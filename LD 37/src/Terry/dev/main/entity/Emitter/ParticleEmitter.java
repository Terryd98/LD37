package Terry.dev.main.entity.Emitter;

import Terry.dev.main.entity.particle.Particle;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;

public class ParticleEmitter extends Emitter {

	public ParticleEmitter(int x, int y, int amount, int lifeTime, Level level, Sprite sprite) {
		super(x, y, Type.PARTICLE, amount, level);
		for (int i = 0; i < amount; i++) {
			level.add(new Particle(x, y, lifeTime, sprite));
			level.remove(this);
		}
	}

}
