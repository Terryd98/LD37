package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.util.Vector2i;

public class StairEntity extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	private int tick = 0;
	public static boolean inRange = false;
	public boolean up;

	// add fuel
	public StairEntity(double x, double y, Level level, boolean up) {
		this.x = x;
		this.y = y;
		sprite = Sprite.stairs;
		this.up = up;
	}

	public StairEntity(Vector2i vector, Level level) {
		this.x = vector.x;
		this.y = vector.y;
		sprite = Sprite.stairs;
	}

	public void tick() {
		tick++;
		List<Player> players = level.getPlayers(this, 16);
		if (players.size() > 0) {
			inRange = true;
		}

	}

	public void render(Render render) {
		render.render((int) x, (int) y, sprite, false, false);
	}
}
