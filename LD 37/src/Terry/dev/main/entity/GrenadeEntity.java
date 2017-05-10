package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.particle.Particle;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class GrenadeEntity extends Entity {

	private Sprite sprite;
	private int time = 15;
	private int tick = 0;
	public static int amount = 0;
	private int fuseTime = 250;
	public static boolean removed = false;
	private int col = 0;
	private final int SHAKE_TIME = 100;
	private int shakeTime = SHAKE_TIME;
	public static boolean exploding = false;
	private int removeTime = 20;
	private boolean xFlip = random.nextBoolean();
	private boolean yFlip = random.nextBoolean();

	public GrenadeEntity(double x, double y, Level level) {
		Game.playSound("/sounds/reload.wav", -15.0f);
		this.x = x;
		this.y = y;
		sprite = Sprite.GrenadeEntity;
		amount = random.nextInt(5) + 1;
		removed = false;

	}

	public GrenadeEntity(Vector2i vector, Level level, Input input) {
		Game.playSound("/sounds/reload.wav", -15.0f);
		this.x = vector.x;
		this.y = vector.y;
		sprite = Sprite.GrenadeEntity;
		amount = random.nextInt(5) + 1;
		removed = false;

	}

	int fuseTick = 40;

	public void tick() {
		fuseTime--;

		if (tick % 50 == 0)
			fuseTick -= 10;
		if (fuseTick <= 10)
			fuseTick = 10;
		if (fuseTime % fuseTick == 0) {
			col = 0xffffffff;
		} else {
			col = 0;
		}
		if (fuseTime == 0)level.add(new ParticleEmitter((int) x, (int) y, 50, 200, level, Sprite.tntParticle));
		if (fuseTime <= 0) {
			level.setTile((int) (x + 8) / 16, (int) (y + 8) / 16, Tile.water);
			exploding = true;
			removed = true;
			removeTime--;
			if (removeTime <= 0) {
				level.remove(this);
				Game.playSound("/sounds/grenade.wav", -20.0f);
				exploding = false;

			}
		}
		tick++;
		List<Player> ps = level.getPlayers(this, 13);
		//if (ps.size() > 0) {
		//	Game.playSound("/sounds/reload.wav", -15.0f);
		//}
	}

	public void render(Render render) {
		if (fuseTime >0) {
			render.renderFlash((int) x, (int) y, sprite, xFlip, yFlip, col);
		}
	}
}
