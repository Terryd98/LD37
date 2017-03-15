package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class WaterTile extends Tile {

	public int time = 0;
	public int tickCount = 0;
	private Sprite sprite = Sprite.water0;

	public WaterTile(int id) {
		super(id);
	}

	protected void tick() {

	}
//TODO: FIX FIX FIX FIX problem with coords rounding and block placing with collision
	public void render(int x, int y, Render render) {
		tickCount++;
		if (tickCount >= 1000000) tickCount = 0;
		if (time >= 1000000) time = 0;
		if ((tickCount % 1200) == 80) time++;
		if (time % 70 > 35) {
			sprite = Sprite.water0;
		} else if (time % 100 > 180)  {
			sprite = Sprite.water1;
		} else {
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

}
