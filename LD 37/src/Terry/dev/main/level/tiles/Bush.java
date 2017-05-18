package Terry.dev.main.level.tiles;

import java.util.Random;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class Bush extends Tile {

	private Sprite sprite = Sprite.bushA;
	
	private Random wRandom = new Random();
	
	public Bush(int id) {
		super(id);
	}

	protected void tick() {
	}

	public void render(int x, int y, Render render) {
		if (tickCount >= (Integer.MAX_VALUE-1000)) tickCount = 0;
		
		if (tickCount % 40 ==0) {
			sprite = Sprite.bushA;
		} else if (tickCount % 20 == 0) {
			sprite = Sprite.bushB;
		}
		render.render(x << 4, y << 4, sprite, false, false);
	}

	public boolean solid() {
		return true;
	}
	public boolean Entitysolid() {
		return false;
	}
}
