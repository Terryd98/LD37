package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class FlowerTile extends Tile {
	
	private Sprite sprite = Sprite.flowerA;
	
	public FlowerTile(int id) {
		super(id);
	}

	protected void tick() {

	}

	public void render(int x, int y, Render render) {
		if (tickCount >= 1000000) tickCount = 0;
		if (tickCount % 40 == 0) {
			sprite = Sprite.flowerA;
		} else if (tickCount % 20 == 0) {
			sprite = Sprite.flowerB;
		}
		render.render(x << 4, y << 4, sprite, false, false);
	}

	public boolean solid() {
		return false;
	}

	public boolean Entitysolid() {
		return false;
	}
	public boolean solidToPlayer() {
		return false;
	}
}
