package Terry.dev.main.entity.mob;

import Terry.dev.main.Game;
import Terry.dev.main.entity.Entity;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.gun.Projectile;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;

public class Mob extends Entity {

	public int dir;
	public Input input;
	public int anim = 0;
	public boolean walking = false;
	public boolean running = false;
	public boolean still = true;
	public static boolean canMove = false;

	public void move(double xa, double ya) {
		if (xa < 0) dir = 0;
		if (xa > 0) dir = 2;
		if (ya < 0) dir = 1;
		if (ya > 0) dir = 3;
		if(collision(xa, ya))canMove =false;
		
		
		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!collision(abs(xa), ya)) {
					this.x += abs(xa);
					canMove = true;
				}
				xa -= abs(xa);
			} else {
				if (!collision(abs(xa), ya)) {
					this.x += xa;
					canMove = true;
				}
				xa = 0;
			}
		}

		while (ya != 0) {
			if (Math.abs(ya) > 1) {
				if (!collision(xa, abs(ya))) {
					this.y += abs(ya);
					canMove = true;
				}
				ya -= abs(ya);
			} else {
				if (!collision(xa, abs(ya))) {
					this.y += ya;
					canMove = true;
				}
				ya = 0;
			}
		}
		
		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!collision(abs(xa), ya)) {
					this.x += abs(xa);
					canMove = true;
				}
				xa -= abs(xa);
			} else {
				if (!collision(abs(xa), ya)) {
					this.x += xa;
					canMove = true;
				}
				xa = 0;
			}
		}

	}
	
	public void move2(double xa, double ya) {
		if (xa < 0) dir = 0;
		if (xa > 0) dir = 2;
		if (ya < 0) dir = 1;
		if (ya > 0) dir = 3;
		if(playerCollision(xa, ya))canMove =false;
		
		
		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!playerCollision(abs(xa), ya)) {
					this.x += abs(xa);
					canMove = true;
				}
				xa -= abs(xa);
			} else {
				if (!playerCollision(abs(xa), ya)) {
					this.x += xa;
					canMove = true;
				}
				xa = 0;
			}
		}

		while (ya != 0) {
			if (Math.abs(ya) > 1) {
				if (!playerCollision(xa, abs(ya))) {
					this.y += abs(ya);
					canMove = true;
				}
				ya -= abs(ya);
			} else {
				if (!playerCollision(xa, abs(ya))) {
					this.y += ya;
					canMove = true;
				}
				ya = 0;
			}
		}
		
		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!playerCollision(abs(xa), ya)) {
					this.x += abs(xa);
					canMove = true;
				}
				xa -= abs(xa);
			} else {
				if (!playerCollision(abs(xa), ya)) {
					this.x += xa;
					canMove = true;
				}
				xa = 0;
			}
		}

	}

	protected int abs(double value) {
		if (value < 0) return -1;
		return 1;
	}

	public void shoot(double x, double y, double dir, int gun) {
		// 1-pistol
		// 2-shotGun
		// 3-assaultRifle
		if (gun == 1) {
			Projectile p = new PistolBullet(x, y, dir);
			level.add(p);
			Game.playSound("/sounds/Shoot.wav", -15.0f);
		}

		if (gun == 3) {
			Projectile p = new AssaultRifle(x, y, dir);
			level.add(p);
			Game.playSound("/sounds/Shoot_old.wav", -15.0f);
		}
	}

	public void clear() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved()) level.getProjectiles().remove(i);

		}
	}

	protected boolean collision(double xa, double ya) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - c % 2 / 1 - 8) / 16;
			double yt = ((y + ya) - c / 2 * 15 / 6 + 1) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			if (level.getTile(ix, iy).solid()) solid = true;
		}
		return solid;
	}
	

	protected boolean playerCollision(double xa, double ya) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - c % 2 / 1 - 8) / 16;
			double yt = ((y + ya) - c / 2 * 15 / 6 + 1) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			if (level.getTile(ix, iy).solidToPlayer()) solid = true;
		}
		return solid;
	}

	/*
	 * public boolean collision(double xa, double ya) { boolean solid = false;
	 * for (int c = 0; c < 4; c++) { double xt = (((x + xa) + c % 2 * 15-4) /
	 * Sprite.TSIZE); double yt = (((y + ya) + c / 2 * 9+6) / Sprite.TSIZE); if
	 * (y <= 0) y = 0; if (x <= 0) x = 0; if (level.getTile((int) xt, (int)
	 * yt).solid()) { return solid = true; } } return solid; }
	 */
	public void tick() {
	}

	public void render(Render render) {

	}

	public void findStartPos(Level level) {
		while (true) {
			int x = random.nextInt(level.width);
			int y = random.nextInt(level.height);
			if (!level.getTile(x, y).solid()) {
				this.x = (x * 16) + 7;

				this.y = (y * 16);
				return;
			}
		}
	}

	public void findZombieStartPos(Level level) {
		while (true) {
			double x = random.nextInt(level.width);
			double y = random.nextInt(level.height);
			if (level.getTile((int) x, (int) y) == Tile.grass) {
				this.x = (int) ((x * 16) + 7);
				this.y = (int) (y * 16);
				return;
			}
		}
	}

}
