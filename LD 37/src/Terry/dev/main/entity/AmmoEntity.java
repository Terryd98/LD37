package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class AmmoEntity extends Entity {

	private Sprite sprite;
	private int time = 15;
	private int tick = 0;
	public static int amount = 0;
	public static boolean removed = false;
	public static double xx, yy;

	public AmmoEntity(double x, double y, Level level) {
		this.x = x;
		this.y = y;
		sprite = Sprite.AmmoEntity;
		amount = random.nextInt(5)+1;
		removed = false;
	}

	public AmmoEntity(Vector2i vector, Level level, Input input) {
		this.x = vector.x;
		this.y = vector.y;
		sprite = Sprite.AmmoEntity;
		amount = random.nextInt(5)+1;
		removed = false;
	}

	public void tick() {
		xx = x;
		yy = y;
		tick++;
		List<Player> ps = level.getPlayers(this, 13);
		if (ps.size() > 0) {
			if (Player.assaultRifle) Player.ASSAULT_RIFLE_AMMO += amount;
			if (Player.pistol) Player.PISTOL_AMMO += amount;
			Player.addedAmmo += amount;
			Game.playSound("/sounds/reload.wav", -15.0f);
			removed = true;
			level.remove(this);
		}
	}

	public void render(Render render) {
		render.render((int) x - 5, (int) y, sprite, false, false);
	}
}
