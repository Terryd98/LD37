package Terry.dev.main.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Terry.dev.main.entity.Entity;
import Terry.dev.main.entity.gun.Projectile;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.entity.particle.Particle;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class Level {

	public int width, height;
	public int[] tiles;
	public int levelChoice;
	public boolean cleared = false;
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Player> players = new ArrayList<Player>();
	public static List<Zombie> zombies = new ArrayList<Zombie>();
	public static List<Projectile> projectiles = new ArrayList<Projectile>();
	public static List<Particle> particles = new ArrayList<Particle>();

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

	public void clearLevel() {
		for (int i = 0; i < entities.size(); i++) {
			entities.remove(i);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.remove(i);
		}
		for (int i = 0; i < players.size(); i++) {
			players.remove(i);
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
	}

	public void loadLevel(String path) {
	}

	protected void ttpLevel() {

	}

	private Random random = new Random();

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}

		for (int i = 0; i < players.size(); i++) {
			players.get(i).tick();
		}

		for (int i = 0; i < zombies.size(); i++) {
			zombies.get(i).tick();
		}

		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).tick();
		}

		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).tick();
		}

	}

	public List<Entity> getEntities(Entity e, int radius) {
		List<Entity> result = new ArrayList<Entity>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			int x = entity.getX();
			int y = entity.getY();

			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(entity);
		}
		return result;
	}

	public List<Zombie> getZombies(int ex, int ey, int radius) {
		List<Zombie> result = new ArrayList<Zombie>();
		for (int i = 0; i < zombies.size(); i++) {
			Zombie zombie = zombies.get(i);
			int x = zombie.getX();
			int y = zombie.getY();

			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(zombie);
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

	public List<Player> getPlayers(Entity e, int radius) {
		List<Player> result = new ArrayList<Player>();
		int ex = e.getX();
		int ey = e.getY();
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			int x = player.getX();
			int y = player.getY();

			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance <= radius) result.add(player);
		}
		return result;
	}

	public void add(Entity e) {
		e.init(this);
		if (e instanceof Player) {
			players.add((Player) e);
		} else if (e instanceof Zombie) {
			zombies.add((Zombie) e);
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

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(render);
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(render);
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(render);
		}

		for (int i = 0; i < zombies.size(); i++) {
			zombies.get(i).render(render);
		}
		for (int i = 0; i < players.size(); i++) {
			players.get(i).render(render);
		}
	}

	// 526B4A = grass
	// A0A4A3 = wall;
	// 404040 = wallIso;
	// 808080 = walllFront
	// 322015 = wood;
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return Tile.water;
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
		if (tiles[x + y * width] == 0xffA0A4A3) tiles[x + y * width] = Tile.wall.id;
		if (tiles[x + y * width] == 0xff404040) tiles[x + y * width] = Tile.wallIso.id;
		if (tiles[x + y * width] == 0xff808080) tiles[x + y * width] = Tile.wallFront.id;
		if (tiles[x + y * width] == 0xff322015) tiles[x + y * width] = Tile.wood.id;
		return Tile.tiles[tiles[x + y * width]];
	}
}
