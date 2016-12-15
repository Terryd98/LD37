package Terry.dev.main.entity.Emitter;

import Terry.dev.main.entity.Entity;
import Terry.dev.main.level.Level;

public class Emitter extends Entity {

	public enum Type {
		MOB, PARTICLE, DEFAULT_BULLET, PISTOL_BULLET

	}

	private Type type;

	public Emitter(int x, int y, Type type, int amount, Level level) {
		this.x = x;
		this.y = y;
		this.type = type;
		init(level);

	}
}
