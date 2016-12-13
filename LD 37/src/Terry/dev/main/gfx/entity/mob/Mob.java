package Terry.dev.main.gfx.entity.mob;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.gfx.entity.DefaultGun;
import Terry.dev.main.gfx.entity.Entity;
import Terry.dev.main.gfx.entity.Projectile;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;

public class Mob extends Entity {

	public int dir;
	public Input input;
	public int anim = 0;
	public boolean walking = false;

	public void move(double xa, double ya) {
		if (xa < 0) dir = 0;
		if (xa > 0) dir = 2;
		if (ya < 0) dir = 1;
		if (ya > 0) dir = 3;
		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (!collision(abs(xa), ya)) {
					this.x += abs(xa);
				}
				xa -= abs(xa);
			} else {
				if (!collision(abs(xa), ya)) {
					this.x += xa;
				}
				xa = 0;
			}
		}

		while (ya != 0) {
			if (Math.abs(ya) > 1) {
				if (!collision(xa, abs(ya))) {
					this.y += abs(ya);
				}
				ya -= abs(ya);
			} else {
				if (!collision(xa, abs(ya))) {
					this.y += ya;
				}
				ya = 0;
			}
		}
	}

	protected int abs(double value) {
		if (value < 0) return -1;
		return 1;
	}

	protected void shoot(double x, double y, double dir) {
		Projectile p = new DefaultGun(x, y, dir);
		Game.playSound("/sounds/Shoot.wav", -20.0f);
		level.add(p);
	}

	public void clear() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved()) level.getProjectiles().remove(i);

		}
	}

	public boolean collision(double xa, double ya) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = (((x + xa) + c % 2 * 15 - 7) / Sprite.TSIZE);
			double yt = (((y + ya) + c / 2 * 9 +6) / Sprite.TSIZE);
			if (y < 0) y = 0;
			if (x <= 0) x = 0;
			if (level.getTile((int) xt, (int) yt).solid()) {
				return solid = true;
			}
		}
		return solid;
	}

	public void tick() {
	}

	public void render(Render render) {
		
	}
	
	public void findStartPos(Level level) {
		while (true) {
			int x = random.nextInt(level.width);
			int y = random.nextInt(level.height);
			if (!level.getTile(x, y).solid()) {
				this.x = (x * 16)+7;
				this.y = (y * 16);
				return;
			}
		}
	}

}
