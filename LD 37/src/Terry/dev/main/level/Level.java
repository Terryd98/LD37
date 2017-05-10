package Terry.dev.main.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import Terry.dev.main.entity.Entity;
import Terry.dev.main.entity.gun.Projectile;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.entity.particle.Particle;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.util.Vector2i;

public class Level {

	public int width, height;
	public int[] tiles;
	public int levelChoice;
	private int time = 0;
	public boolean cleared = false;
	public boolean levelSwitching = false;

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Player> players = new ArrayList<Player>();
	public static List<Zombie> zombies = new ArrayList<Zombie>();
	public static List<ChasingZombie> chasingZombies = new ArrayList<ChasingZombie>();
	public static List<Projectile> projectiles = new ArrayList<Projectile>();
	public static List<Particle> particles = new ArrayList<Particle>();

	private Comparator<Entity> entitySorter = new Comparator<Entity>() {
		public int compare(Entity e0, Entity e1) {
			if (e1.y < e0.y) return +1;
			if (e1.y > e0.y) return -1;

			return 0;
		}
	};

	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;
			if (n1.fCost > n0.fCost) return -1;

			return 0;
		}
	};

	private Comparator<Zombie> zombieSorter = new Comparator<Zombie>() {
		public int compare(Zombie z1, Zombie z2) {
			if (z1.y < z2.y) return -1;
			if (z1.y > z2.y) return +1;
			return 0;
		}
	};

	private Comparator<ChasingZombie> chaserSorter = new Comparator<ChasingZombie>() {
		public int compare(ChasingZombie z1, ChasingZombie z2) {
			if (z1.y < z2.y) return -1;
			if (z1.y > z2.y) return +1;
			return 0;
		}
	};

	public String level1 = "/levels/level1.png";
	public String level2 = "/levels/level.png";

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + y * width] = Tile.grass.id;
			}
		}

	}

	public Level(String path) {
		loadLevel(path);
		ttpLevel();
	}

	public void prepLevelSwitch() {
		removeAll();
		if (removeAll()) cleared = true;
		
	}

	private boolean removeAll() {
		for (int i = 0; i < entities.size(); i++) {
			entities.remove(i);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.remove(i);
		}
		for (int i = 0; i < players.size(); i++) {
			players.remove(i);
		}
		for (int i = 0; i < chasingZombies.size(); i++) {
			chasingZombies.remove(i);
		}
		for (int i = 0; i < zombies.size(); i++) {
			zombies.remove(i);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.remove(i);
		}

		Particle.lremove = true;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				tiles[x + y * width] = 0;
			}
		}

		if (entities.size() == 0 && projectiles.size() == 0 && players.size() == 0 && chasingZombies.size() == 0 && zombies.size() == 0 && particles.size() == 0) return true;
		return false;
	}

	public void loadLevel(String path) {
	}

	protected void ttpLevel() {

	}

	private Random random = new Random();

	public void tick() {
		time++;

		int xft = random.nextInt((width) * 16);
		int yft = random.nextInt((height) * 16);
		// if (time % 60 == 0) System.out.println(random.nextInt((width *
		// height) / 16));
		if (getTile(xft, yft) == Tile.grass) {
			setTile(xft, yft, Tile.flower);
		}

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}

		for (int i = 0; i < players.size(); i++) {
			players.get(i).tick();
		}

		for (int i = 0; i < zombies.size(); i++) {
			zombies.get(i).tick();
		}

		for (int i = 0; i < chasingZombies.size(); i++) {
			chasingZombies.get(i).tick();
		}

		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).tick();
		}

		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).tick();
		}

	}

	public List<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Tile goala = getTile(goal.x, goal.y);
		Node current = new Node(start, null, 0, getDistance(start, goal));
		if (!goala.solid()) openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);

			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = current.tile.x;
				int y = current.tile.y;
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.solid()) continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost = (getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= current.gCost) continue;
				if (!vecInList(openList, a) || gCost < current.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}

	private boolean vecInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
		}
		return false;
	}

	private double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public List<Entity> getEntities(Entity e, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		double ex = e.getX();
		double ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			double x = entity.getX();
			double y = entity.getY();

			double dx = Math.abs(x - ex);
			double dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(entity);
		}
		return result;
	}

	public List<Zombie> getZombies(double ex, double ey, int radius) {
		List<Zombie> result = new ArrayList<Zombie>();
		for (int i = 0; i < zombies.size(); i++) {
			Zombie zombie = zombies.get(i);
			double x = zombie.getX();
			double y = zombie.getY();

			double dx = Math.abs(x - ex);
			double dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(zombie);
		}
		return result;
	}

	public List<Player> getPlayers(Entity e, int radius) {
		List<Player> result = new ArrayList<Player>();
		double ex = e.getX();
		double ey = e.getY();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			double x = player.getX();
			double y = player.getY();

			double dx = Math.abs(x - ex);
			double dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(player);
		}
		return result;
	}

	public List<ChasingZombie> getChaserZombies(double ex, double ey, int radius) {
		List<ChasingZombie> result = new ArrayList<ChasingZombie>();
		for (int i = 0; i < chasingZombies.size(); i++) {
			ChasingZombie chasingZombie = chasingZombies.get(i);
			double x = chasingZombie.getX();
			double y = chasingZombie.getY();

			double dx = Math.abs(x - ex);
			double dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(chasingZombie);
		}
		return result;
	}

	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			int xt = ((x - c % 2 * size + xOffset) / Sprite.TSIZE);
			int yt = ((y - c / 2 * size + yOffset) / Sprite.TSIZE);
			if (x < 0) x = 0;
			if (y < 0) y = 0;
			if (getTile(xt, yt).Entitysolid()) {
				return solid = true;
			}
		}
		return solid;
	}

	public void add(Entity e) {
		e.init(this);
		if (e instanceof Player) {
			players.add((Player) e);
		} else if (e instanceof Zombie) {
			zombies.add((Zombie) e);
		} else if (e instanceof ChasingZombie) {
			chasingZombies.add((ChasingZombie) e);
		} else if (e instanceof Projectile) {
			projectiles.add((Projectile) e);
		} else if (e instanceof Particle) {
			particles.add((Particle) e);
		} else {
			entities.add(e);
		}
	}

	public void remove(Entity e) {
		if (e instanceof Player) {
			players.remove((Player) e);
		} else if (e instanceof Zombie) {
			zombies.remove((Zombie) e);
		} else if (e instanceof ChasingZombie) {
			chasingZombies.remove((ChasingZombie) e);
		} else if (e instanceof Projectile) {
			projectiles.remove((Projectile) e);
		} else if (e instanceof Particle) {
			particles.remove((Particle) e);
		} else {
			entities.remove(e);
		}
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public void render(int xScroll, int yScroll, Render render) {
		render.setOffsets(xScroll, yScroll);
		int x0 = xScroll >> 4;
		int x1 = (xScroll + render.width + Sprite.TSIZE) >> 4;
		int y0 = yScroll >> 4;
		int y1 = (yScroll + render.height + Sprite.TSIZE) >> 4;

		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, render);
			}
		}

		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(render);
		}

		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(render);
		}
		for (int i = 0; i < entities.size(); i++) {
			Collections.sort(entities, entitySorter);
			entities.get(i).render(render);
		}
		for (int i = 0; i < zombies.size(); i++) {
			Collections.sort(zombies, zombieSorter);
			zombies.get(i).render(render);
		}

		for (int i = 0; i < chasingZombies.size(); i++) {
			Collections.sort(chasingZombies, chaserSorter);
			chasingZombies.get(i).render(render);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render(render);
		}

	}

	public Player getPlayerAt(int index) {
		return players.get(index);

	}

	// 526B4A = grass
	// A0A4A3 = wall;
	// 404040 = wallIso;
	// 808080 = walllFront
	// 322015 = wood;
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			if (cleared) return Tile.voidTile;
			return Tile.water;
		}
		if (tiles[x + y * width] == 0xff526B4A) tiles[x + y * width] = Tile.grass.id;
		if (tiles[x + y * width] == 0xff638200) tiles[x + y * width] = Tile.grassCornerTL.id;
		if (tiles[x + y * width] == 0xff4A6000) tiles[x + y * width] = Tile.grassCornerTR.id;
		if (tiles[x + y * width] == 0xff7FA500) tiles[x + y * width] = Tile.grassCornerBL.id;
		if (tiles[x + y * width] == 0xff698900) tiles[x + y * width] = Tile.grassCornerBR.id;
		if (tiles[x + y * width] == 0xff526B00) tiles[x + y * width] = Tile.grassLeft.id;
		if (tiles[x + y * width] == 0xff006023) tiles[x + y * width] = Tile.grassUp.id;
		if (tiles[x + y * width] == 0xffC3FF00) tiles[x + y * width] = Tile.grassRight.id;
		if (tiles[x + y * width] == 0xff009637) tiles[x + y * width] = Tile.grassDown.id;
		if (tiles[x + y * width] == 0xff87845E) tiles[x + y * width] = Tile.rottenHead.id;
		if (tiles[x + y * width] == 0xffD8D8D8) tiles[x + y * width] = Tile.basementFloor.id;
		if (tiles[x + y * width] == 0xffFAFA37) tiles[x + y * width] = Tile.flower.id;
		if (tiles[x + y * width] == 0xff3A607D) tiles[x + y * width] = Tile.water.id;
		if (tiles[x + y * width] == 0xffA0A4A3) tiles[x + y * width] = Tile.wall.id;
		if (tiles[x + y * width] == 0xff676666) tiles[x + y * width] = Tile.stone.id;
		if (tiles[x + y * width] == 0xff161616) tiles[x + y * width] = Tile.bridgeWallTop.id;
		if (tiles[x + y * width] == 0xffD3D3D3) tiles[x + y * width] = Tile.bridgeWallBottom.id;
		if (tiles[x + y * width] == 0xff404040) tiles[x + y * width] = Tile.wallIso.id;
		if (tiles[x + y * width] == 0xff808080) tiles[x + y * width] = Tile.wallFront.id;
		if (tiles[x + y * width] == 0xff322015) tiles[x + y * width] = Tile.wood.id;
		return Tile.tiles[tiles[x + y * width]];
	}

	public void setTile(int x, int y, Tile tileReplace) {
		if (x < 0 || y < 0 || x >= width || y >= height) return;
		tiles[x + y * width] = tileReplace.id;
	}
}
