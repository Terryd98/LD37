package Terry.dev.main.gfx.entity;

import java.util.List;
import Terry.dev.main.Game;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.gfx.entity.Emitter.ParticleEmitter;
import Terry.dev.main.gfx.entity.mob.Zombie;

public class DefaultGun extends Projectile {

	public static final int FIRERATE = 10;
	public static final int AMMO = 20;
	public static final int CLIP = 100;

	public DefaultGun(double x, double y, double dir) {
		super(x, y, dir);
		range = random.nextInt(200) + 50;
		speed = 5;
		damage = 20;

		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}

	public void tick() {

		List<Zombie> zombies = level.getZombies((int) x + (int) nx + 10, (int) y + (int) ny + 10, 20);
		if (zombies.size() > 0) {
			remove();
			Zombie zombie = zombies.get(0);
			zombie.hurt(damage);

			Game.playSound("/sounds/hit.wav", -15.0f);
		}

		move();
		if (level.tileCollision((int) (x + nx), (int) (y + ny), 4, 5, 6)) {
			level.add(new ParticleEmitter((int) x, (int) y, 50, 70, level, Sprite.particle));
			Game.playSound("/sounds/hit.wav", -15.0f);
			remove();
		}

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

	public void render(Render render) {

		render.render((int) x, (int) y, Sprite.projectile, false, false);

	}

}
