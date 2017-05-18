package Terry.dev.main.entity;

import java.util.List;
import Terry.dev.main.Game;
import Terry.dev.main.entity.mob.Player;
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
			Player.addAmmo(amount);
			removed = true;
			level.remove(this);
		}
		if (level.getTile((int) x / 16, (int) y / 16) == Tile.voidTile || level.getTile((int) x / 16, (int) y / 16) == Tile.water) {
			x-=0.5;
			if (tick % 10 == 0) {
				y += 0.3;
			} else if(tick %5 == 0){
				y -= 0.3;
			}
		}
	}

	public void render(Render render) {
		render.render((int) x - 5, (int) y, sprite, false, false);
	}
}
