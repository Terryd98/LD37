package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.tiles.VaultTile;

public class VaultEntity extends Entity {

	public static boolean removed = false;
	public static double xx, yy;
	private int time = 0;
	public static boolean opened = false;
	public static boolean opening = false;
	private int openTime = 30;

	public VaultEntity(Level level) {
		x = VaultTile.x * 16;
		y = VaultTile.y * 16;
	}

	public void tick() {
		time++;
		if (x == 0 && y == 0) {
			x = VaultTile.x * 16;
			y = (VaultTile.y * 16) - 32 ;
		}
		List<Player> ps = level.getPlayers(this, 60);
		if (ps.size() > 0 && Player.hasKey) {
			
			if(openTime !=0){
				opening = true;
				openTime--;
			}
			
			if(openTime <= 0) {
				opening = false;
				openTime =0;
				opened = true;
				Player.hasKey = false;
			}
			System.out.println("AT VAULT");
		}

	}

	public void render(Render render) {
		 //render.render((int) x, (int) y, Sprite.KeyEntity, false, false);
	}
}
