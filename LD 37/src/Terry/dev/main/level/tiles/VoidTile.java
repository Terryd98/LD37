package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class VoidTile extends Tile {

	public VoidTile(int id) {
		super(id);
	}

	protected void tick() {
		
	}

	public void render1(int x, int y, Render render) {
		render.render(x << 4, y << 4, Sprite.voidSprite , false, false);
	}

	public boolean solid() {
		return true;
	}
	public boolean Entitysolid() {
		return false;
	}
}
