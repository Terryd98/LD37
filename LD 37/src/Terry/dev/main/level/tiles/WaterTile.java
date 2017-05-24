package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class WaterTile extends Tile {

	private Sprite sprite = Sprite.water0;

	public WaterTile(int id) {
		super(id);
	}

	protected void tick() {

	}
//TODO: FIX FIX FIX FIX problem with coords rounding and block placing with collision
	public void render(int x, int y, Render render) {
		if (tickCount >= 1000000) tickCount = 0;
		if (tickCount % 80 == 0 ) {
			sprite = Sprite.water0;
		} else if (tickCount % 50 == 0)  {
			sprite = Sprite.water1;
		} else if (tickCount % 20 == 0)  {
			sprite = Sprite.water2;
		}
		render.render(x << 4, y << 4, sprite , false, false);
	}

	public boolean solid() {
		return true;
	}
	
	public boolean Entitysolid() {
		return false;
	}
	public boolean solidToPlayer() {
		return false;
	}
}
