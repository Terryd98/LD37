package Terry.dev.main.level.tiles;

import Terry.dev.main.entity.VaultEntity;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Tile;

public class VaultTile extends Tile {

	public static boolean connected = true;
	public static boolean stop = false;
	public static boolean start = false;
	public static int x, y;
	private Sprite sprite;

	public VaultTile(int id) {
		super(id);
		VaultSpawned = true;
		sprite = Sprite.VaultDoor;

	}

	protected void tick() {

	}

	public void render(int x, int y, Render render) {
		this.x = x;
		this.y = y;
		if (!stop) {
			start = true;
			connected = false;
			// System.out.println(connected);
		}
		if (!connected && !VaultEntity.opened) render.render(x << 4, y << 4, Sprite.Vault_Unconnected, false, false);

		if (connected) {

			if (VaultEntity.opening && !VaultEntity.opened) {
				if (VaultEntity.opening && tickCount % 8 == 0) {
					sprite = Sprite.VaultDoor_opening;
				} else if (VaultEntity.opening && tickCount % 4 == 0) {
					sprite = Sprite.VaultDoor;
				}
			}
			
			if(VaultEntity.opened) {
				sprite = Sprite.VaultDoor_open;
			}

			// System.out.println(x + " | " + y);

			render.renderWH(260 << 4, 45 << 4, sprite, false, false, false);
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
