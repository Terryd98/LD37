package Terry.dev.main.entity.gun;

import java.util.Random;
import Terry.dev.main.entity.Entity;
import Terry.dev.main.gfx.Render;

public class Projectile extends Entity {

	protected double xStart, yStart;
	protected double angle;
	protected double x, y;
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

	protected void move() {
		if (!level.tileCollision((int) (x + nx), (int) (y + ny), 4, 5, 6)) {
			x += nx;
			y += ny;
			if (dist() > range) remove();
		}
	}

	private double dist() {
		double dist = Math.sqrt(Math.abs((xStart - x) * (xStart - x) + (yStart - y) * (yStart - y)));
		return dist;
	}

	public void tick() {
	}

	public void render(Render render) {
	}

}
