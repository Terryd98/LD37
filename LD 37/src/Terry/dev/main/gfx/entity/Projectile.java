package Terry.dev.main.gfx.entity;

import java.util.Random;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.entity.mob.Player;

public class Projectile extends Entity {

	protected double xStart, yStart;
	protected double angle;
	protected double nx, ny;
	protected int speed, range, damage, ammo;
	protected Random random = new Random();

	public Projectile(double x, double y, double dir) {
		this.x = x;
		this.y = y;
		xStart = x;
		yStart = y;
		angle = dir;
	}

	public void updateShooting() {

	}

	public void tick() {
	}

	protected void move() {

	}

	public void render(Render render) {
	}

}
