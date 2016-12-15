package Terry.dev.main.entity.Emitter;

import Terry.dev.main.entity.gun.DefaultGun;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.gun.Projectile;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;

public class ProjectileEmitter extends Emitter {

	public ProjectileEmitter(int x, int y, int amount, double dir, Type type, Level level, Sprite sprite) {
		super(x, y, type, amount, level);
		for (int i = 0; i < amount; i++) {
			if (type == Type.PISTOL_BULLET) {
				Projectile p = new PistolBullet(x, y, dir);
				level.add(p);
			}
			if (type == Type.DEFAULT_BULLET) {
				Projectile p = new DefaultGun(x, y, dir);
				level.add(p);
			}
		}
	}

}
