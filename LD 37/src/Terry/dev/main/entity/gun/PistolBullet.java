package Terry.dev.main.entity.gun;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class PistolBullet extends Projectile {

	public PistolBullet(double x, double y, double dir) {
		super(x, y, dir);
		range = random.nextInt(200) + 50;
		speed = 5;
		damage = 20;

		nx = speed * Math.cos(angle);
		ny = speed * Math.sin(angle);
	}

	public void tick() {
		move();
	}
	
	protected void shoot(double x, double y, double dir) {
		Projectile p = new DefaultGun(x, y, dir);
		Game.playSound("/sounds/Shoot.wav", -20.0f);
		level.add(p);
		
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

	public void render(Render render) {
		render.render((int) x, (int) y, Sprite.projectile, false, false);
	}

}
