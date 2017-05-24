package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class TreeTrunk extends Tile {


	public TreeTrunk(int id) {
		super(id);
	}

	protected void tick() {
		
	}

	public void render(int x, int y, Render render) {
		render.render(x << 4, y << 4, Sprite.treeTrunk, false, false);
	}

	public boolean solid() {
		return true;
	}
	public boolean Entitysolid() {
		return true;
	}public boolean solidToPlayer() {
		return true;
	}
}
