package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class TreeEntity extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	public boolean inAir = false;
	public boolean dir = false;
	private boolean broken = false;
	private int health = 20;
	private int sound = 10;
	private int tick = 0;
	private int tickCount = 0;
	private int TYPE = 0;

	public TreeEntity(double x, double y, Level level) {
		this.x = x * 16;
		this.y = y * 16;
		if (random.nextInt(2) == 0) {
			TYPE = 0;
			sprite = Sprite.tallTreeA;
			level.setTile((int) x, (int) y, Tile.treeTruck);
		} else {
			TYPE = 1;
			sprite = Sprite.tallTree1A;
			level.setTile((int) x, (int) y, Tile.treeTruck1);
		}
		dir = random.nextBoolean();
	}

	public TreeEntity(Vector2i vector, Level level) {
		this.x = vector.x * 16;
		this.y = vector.y * 16;
		sprite = Sprite.tallTreeA;
	}

	public void tick() {
		tickCount++;
		if (tickCount % 15 == 0) {
			if (tick < 2) {
				tick++;
			} else {
				tick = 0;
			}
		}

		if (tick == 0) {
			if (TYPE == 0) sprite = Sprite.tallTreeA;
			if (TYPE == 1) sprite = Sprite.tallTree1A;
		} else if (tick == 1) {
			if (TYPE == 0) sprite = Sprite.tallTreeB;
			if (TYPE == 1) sprite = Sprite.tallTree1B;
		} else if (tick == 2) {
			if (TYPE == 0) sprite = Sprite.tallTreeC;
			if (TYPE == 1) sprite = Sprite.tallTree1C;
		}

		List<Player> players = level.getPlayers(this, 25);
		if (players.size() > 0) {
		} else {

		}
		if (sound > 0) sound--;
	}

	public void chop() {
		if (sound <= 0) {
			Game.playSound("/sounds/flower.wav", 0.0f);
			sound = 10;
		}
		health -= 3;
		if (health <= 0) {
			int logs = random.nextInt(4);
			if(logs == 0) logs ++;
			if (TYPE == 0) level.add(new ParticleEmitter((int) x, (int) y - 10, logs, 2000, level, Sprite.logParticle));
			else if (TYPE == 1) level.add(new ParticleEmitter((int) x, (int) y - 10, logs, 2000, level, Sprite.logParticle1));
			level.remove(this);
		}
	}

	public void render(Render render) {
		// render.renderIcon((int)x, (int)y, sprite.water0, false, false, true);
		if (!broken) {
			render.renderWH((int) x - 16 / 2, (int) y - (64 - 16), sprite, dir, false, false);
		}
	}
}
