package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.util.Vector2i;

public class CommandCentre extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	public static boolean activated = false;
	private boolean powered = false;
	private int tick = 0;
	public static boolean inRange = false;
	public boolean inAir = false;
	private boolean tutorial = true;
	private int tutLength = 100;

	// add fuel
	public CommandCentre(double x, double y, Level level) {
		this.x = x;
		this.y = y;
		sprite = Sprite.command_centre_off;
	}

	public CommandCentre(Vector2i vector, Level level) {
		this.x = vector.x;
		this.y = vector.y;
		sprite = Sprite.command_centre_off;
	}

	public boolean beep = false;
	public boolean pickupRange = false;

	public void tick() {
		/*if (level.getTile((int) x / 16, (int) y / 16) == Tile.wood) {
			powered = true;
		} else {
			powered = false;
		}*/
		powered= true;

		if (tutorial) {
			if (tutLength > 0) tutLength--;
		}
		
		tick++;
		List<Player> players = level.getPlayers(this, 50);
		List<Player> ps = level.getPlayers(this, 23);
		if (ps.size() > 0) {
			pickupRange = true;
		} else {
			pickupRange = false;
		}
		if (players.size() > 0 && powered) {
			inRange = true;
			if (tick % 100 < 50) {
				sprite = Sprite.command_centre_on;
				if (beep) {
					for (int i = 0; i < 1; i++) {
						beep = false;
						Game.playSound("/sounds/beep.wav", -25.0f);
						beep = false;
					}
				}
			} else {
				beep = true;
				sprite = Sprite.command_centre_flash;
			}
		} else {
			inRange = false;
			activated = false;
			sprite = Sprite.command_centre_off;
		}

	}

	public void render(Render render) {
		List<Player> players = level.getPlayers(this, 50);
		if (players.size() > 0 && tutLength ==0) {
			tutorial = true;
			if (tick % 50 < 25) {
				render.render((int) x + 16, (int) y , Sprite.f_indicator, false, false);
			} else {
				render.render((int) x + 16, (int) y - 2, Sprite.f_indicator, false, false);
			}
		} else {
			tutorial = false;
			tutLength = 100;
		}

		if (inAir) {
			render.render((int) x, (int) y + 15, Sprite.command_centre_shadow, false, false);
		} else {
			render.render((int) x, (int) y + 11, Sprite.command_centre_shadow, false, false);
		}
		render.render((int) x, (int) y, sprite, false, false);
		render.render((int) x, (int) y+16, Sprite.command_centre_legs, false, false);

	}
}
