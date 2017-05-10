package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.entity.DrawerEntity;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class LootingMenu extends Menu {

	private String returnB = "Return", volume = "Volume";
	private String[] selections = { "return", "Volume" };
	private int bgCol = 0;

	private Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	public LootingMenu() {
		selected = 0;
		bgCol = 0x1b1b1b;
		sprite = Sprite.title;

	}

	public void returnB() {
		game.setMenu(null);
	}

	public void volume() {

	}

	public void tick() {
		time++;
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
			render.renderRect(5, 11, 6 * 16, 9 * 16, 0x6B6B6B, false);
			for (int i = 0; i < 6; i++) {
			//	render.renderIcon(10 + i * 16, yp, Sprite.guiFull, false, false, false);
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
		Font.draw("LOOTING", render, 16 * 2 , 25 - 1, 0xDB76A5, false);

		if (selected == 0) {
		}

		if (selected == 1) {
		}
	}
}
