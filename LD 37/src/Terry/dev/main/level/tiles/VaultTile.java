package Terry.dev.main.level.tiles;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;

public class VaultTile extends Tile {

	public static boolean connected = true;
	public static boolean stop = false;
	public static boolean start = false;
	public static int x, y;

	public VaultTile(int id) {
		super(id);
	}

	protected void tick() {

	}

	public void render(int x, int y, Render render) {
		this.x = x;
		this.y = y;
		if (!stop) {
			start = true;
			connected = false;
			//System.out.println(connected);
		}
		render.render(x << 4, y << 4, Sprite.Vault_Unconnected, false, false);

		if (connected) {
			//System.out.println(x + " | " + y);
			render.renderWH(260<< 4, 45 << 4, Sprite.VaultDoor, false, false, false);
		}
	}

	public boolean solid() {
		return true;
	}

	public boolean Entitysolid() {
		return true;
	}
	public boolean solidToPlayer() {
		return true;
	}
}
