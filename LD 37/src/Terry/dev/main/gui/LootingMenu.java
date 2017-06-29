package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.entity.DrawerEntity;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class LootingMenu extends Menu {

	private String[] selections = { "return", "Volume" };
///////////////////sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss// MOVE TO CUPBAORD 
	public Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	private int cashLoot = random.nextInt(10);
	private int ammoLoot = random.nextInt(5);

	public int cashLooted = 0;
	public int ammoLooted = 0;
	public boolean empty = false;

	public LootingMenu() {
		selected = 0;
		sprite = Sprite.title;

	}

	public void returnB() {
		game.setMenu(null);
	}

	public void volume() {

	}

	public void tick() {
		time++;
		int r1 = random.nextInt(1000);
		int r2 = random.nextInt(1500);
		if (r1 != 0 && time % r1 == 0 && cashLoot > 0) {
			int cash = random.nextInt(20);
			if (cash != 0 ) {
				Player.addCash(cash);
				cashLoot--;
				cashLooted++;

			}
		}
		if (cashLoot <= 0 && ammoLoot <= 0) empty = true;
		
		
			
			if (r2 != 0 && time % r2 == 0 && ammoLoot > 0) {
			int ammo = random.nextInt(5);
			if (ammo != 0) {
				Player.addAmmo(ammo);
				ammoLooted++;
				ammoLoot--;
			}
		}

		if (!DrawerEntity.inRange || input.use.clicked) {
			DrawerEntity.looting = false;
			game.setMenu(null);
		}
		if (selected >= selections.length - 1) selected = selections.length - 1;

		selector();
		if (input.space.clicked) select(selected);

		if (time % 10 == 5) anim++;
		if (anim > 3) anim = 0;
		if (anim == 0) sprite = Sprite.title;
		if (anim == 1) {
			drop = 140;
			sprite = Sprite.title1;
		}
		if (anim == 2) sprite = Sprite.title2;
		if (anim == 3) sprite = Sprite.title3;
		if (anim == 3 || anim == 0) {
			if (random.nextInt(2) == 0) {
				drop += 6;
			} else {
				drop += 10;
			}
		}
		if (selected == 0) {
			if (input.right.clicked) {
				Game.VOL_MOD++;

			}
			if (input.left.clicked) {
				Game.VOL_MOD--;
			}
			if (Game.VOL_MOD >= 10) Game.VOL_MOD = 10;
			if (Game.VOL_MOD <= -20) Game.VOL_MOD = -20;
		}
	}

	public void select(int selected) {
		if (selected == 0) volume();
		if (selected == 1) returnB();
	}

	public static boolean canAfford = false;
	public static int ar_price = 500;
	public static int ammo_price = 250;
	public static int trap_price = 50;
	public static int selectTime = 200;
	public static final int SELECT_TIME = 200;

	int yp = 16;
	int selected = 1;

	// selected = 0 - Play
	// selected = 1 - options
	// selected = 2 - help
	// selected = 3 - exit
	public void render(Render render) {
		int yy = 185;
		int gap = 15;

		/*
		 * if (yp <= 32) yp = 32; if (yp >= 9 * 16) yp = 9 * 16; if (selected <=
		 * 1) selected = 1; if (selected >= 8) selected = 8; if
		 * (input.up.clicked ) { yp -= 16; selected--;
		 * Game.playSound("/sounds/reload.wav", -15.0f);
		 * 
		 * } if (input.down.clicked ) { yp += 16; selected++;
		 * Game.playSound("/sounds/reload.wav", -15.0f); }
		 */

		// render.renderRect(3, yp-2, 100, 16, 0x626262);
		{
			render.renderRect(5, 11, 6 * 16, 9 * 16, 0x848484, false);
			for (int i = 0; i < 6; i++) {
				// render.renderIcon(10 + i * 16, yp, Sprite.guiFull, false,
				// false, false);
			}

			render.renderIcon(-6, 6, Sprite.guiCorner, false, false, false);
			render.renderIcon((7 * 16) - 6, 6, Sprite.guiCorner, true, false, false);

			render.renderIcon(-6, 166, Sprite.guiCorner, false, true, false);
			render.renderIcon((7 * 16) - 6, 166, Sprite.guiCorner, true, true, false);

			for (int i = 0; i < 6; i++) {
				render.renderIcon(16 * i + 10, 6, Sprite.guiTop, false, false, false);
			}

			for (int i = 0; i < 6; i++) {
				render.renderIcon(16 * i + 10, 166, Sprite.guiTop, false, true, false);
			}

			for (int i = 0; i < 9; i++) {
				render.renderIcon(-6, 16 * i + 22, Sprite.guiSide, false, true, false);
			}
			for (int i = 0; i < 9; i++) {
				render.renderIcon(6 * 16 + 10, 16 * i + 22, Sprite.guiSide, true, false, false);
			}
		}
		Font.draw("LOOTING", render, (16 * 2), 25, 0x5E3F4E, false);
		Font.draw("LOOTING", render, 16 * 2, 25 - 1, 0xDB76A5, false);

		if (cashLooted > 0) {
			for (int i = 0; i < cashLooted; i++) {
				if (i < 25) {
					render.renderIcon(10, 140 - (i * 5), Sprite.CashEntity, false, false, false);
				}
			}
		}
		

		if (ammoLooted > 0) {
			for (int i = 0; i < ammoLooted; i++) {
				if (i < 25) {
					render.renderIcon(30, 140 - (i * 5), Sprite.AmmoEntity, false, false, false);
				}
			}
		}
		int yE = 157;
		if(empty && time % 55 < 50 / 2) {
			Font.draw("Empty!", render, 37, yE + 1, 0x7E305C, false);
			Font.draw("Empty!", render, 37, yE, 0xEF358C, false);
		}else if(empty){
			Font.draw("Empty!", render, 37, yE + 3, 0x7E305C, false);
			Font.draw("Empty!", render, 37, yE+2, 0xEF358C, false);
		}
		

		if (!empty) {
			int x = 13;
			int y = 160;
			if (time % 55 < 50 / 2) {
				Font.draw("searching", render, x, y + 1, 0x363636, false);
				Font.draw("searching", render, x, y, 0xEFF589, false);
			} else {
				Font.draw("searching...", render, x, y + 1, 0x363636, false);
				Font.draw("searching...", render, x, y, 0xEFF589, false);
			}
		}

		if (selected == 0) {
		}

		if (selected == 1) {
		}
	}
}
