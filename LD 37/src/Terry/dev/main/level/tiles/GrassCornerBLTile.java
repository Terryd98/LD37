package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class GrassCornerBLTile extends Tile {

	public GrassCornerBLTile(int id) {
		super(id);
	}

	protected void tick() {

	}

	public void render(int x, int y, Render render) {
		render.render(x << 4, y << 4, Sprite.grassCorner, false, true);
	}

	public boolean solid() {
		return solid;
	}
	
	public boolean Entitysolid() {
		return false;
	}
	public boolean solidToPlayer() {
		return false;
	}
	
}
