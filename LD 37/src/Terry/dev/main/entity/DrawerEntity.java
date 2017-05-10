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

public class DrawerEntity extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	private int tickCount = 0;
	private int time = 15;
	private int tick = 0;
	public static boolean inRange = false;
	public static boolean looting = false;
	public boolean inAir = false;
	private Input input;

	public DrawerEntity(double x, double y, Level level, Input input) {
		this.input = input;
		this.x = x;
		this.y = y;
		sprite = Sprite.drawer;
	}

	public DrawerEntity(Vector2i vector, Level level, Input input) {
		this.x = vector.x;
		this.y = vector.y;
		this.input = input;
		sprite = Sprite.drawer;
	}

	public boolean beep = false;
	public boolean pickupRange = false;

	public void tick() {
		tick++;
		List<Player> players = level.getPlayers(this, 15);
		if (players.size() > 0) {
			inRange = true;      
		} else {
			looting = false;
			inRange = false;

		}

	}

	public void renderGui(Render render) {
	}

	public void render(Render render) {
		render.render((int) x - 11, (int) y, sprite, false, false);
		renderGui(render);
		if (looting) {
			if (tick % 55 < 50 / 2) {
				Font.draw("Looting", render, (int) x + 10, (int) y+1, 0x363636, true);
				Font.draw("Looting", render, (int) x + 10, (int) y, 0xEFF589, true);
			} else {
				Font.draw("Looting..", render, (int) x + 10, (int) y+1, 0x363636, true);
				Font.draw("Looting..", render, (int) x + 10, (int) y, 0xEFF589, true);
			}
		}

	}
}
