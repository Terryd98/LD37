package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.entity.CommandCentre;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class ShopMenu extends Menu {

	private String returnB = "Return", volume = "Volume", aRifle = "A-RIFLE";
	private String[] selections = { "AMMO", "TRAP", "A-RIFLE" };
	private int bgCol = 0;

	private Sprite sprite;
	private int time = 0;
	private int anim = 0;
	int yp = 32;
	private int drop = 140;
	private Random random = new Random();

	public ShopMenu() {
		selected = 0;
		bgCol = 0x1b1b1b;
		sprite = Sprite.title;
	}

	public void tick() {
		time++;
		if (CommandCentre.inRange && input.use.clicked) {
			game.setMenu(null);
			CommandCentre.activated = false;
		}
		if (selected >= selections.length - 1) {
			cursorCanMoveDown = false;
		}
		else{
			cursorCanMoveDown = true;
		}
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
	}

	public void select(int selected) {
		if (selected == 0) buyAmmo();
		if (selected == 1) buyTrap();
		if (selected == 2) buyAr();
	}

	public void buyAmmo() {
		if (Player.cash >= ammo_price && (Player.assaultRifle || Player.pistol || Player.shotgun)) {
			Player.ASSAULT_RIFLE_AMMO = AssaultRifle.AMMO;
			Player.PISTOL_AMMO = PistolBullet.AMMO;
			Player.cash -= ammo_price;
		}
		if ((Player.ASSAULT_RIFLE_AMMO >= AssaultRifle.AMMO || Player.PISTOL_AMMO >= PistolBullet.AMMO) ) {
			Game.playSound("/sounds/hit.wav", -5.0f);
		}
	}

	private void buyTrap() {
		if (Player.cash >= trap_price) {
			Player.cash -= trap_price;
			Player.traps++;
		}
		if ((Player.ASSAULT_RIFLE_AMMO >= AssaultRifle.AMMO || Player.PISTOL_AMMO >= PistolBullet.AMMO)) {
			Game.playSound("/sounds/hit.wav", -5.0f);
		}
	}

	private void buyAr() {
		if (Player.cash >= ar_price && !Player.assaultRifle) {
			Player.pistol = false;
			Player.assaultRifle = true;
			Player.hasAssaultRifle = true;
			Player.cash -= ar_price;
		}
		if ((Player.cash <= ar_price || Player.assaultRifle)) {
			Game.playSound("/sounds/hit.wav", -5.0f);
		}
	}

	public static boolean canAfford = false;
	public static int ar_price = 500;
	public static int ammo_price = 250;
	public static int trap_price = 50;

	public void render(Render render) {
		render.renderRect(5, 11, 6 * 16, 9 * 16, 0x848484, false);

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

		Font.draw("SHOP", render, (16 * 2 + 10), 25, 0x5E3F4E, false);
		Font.draw("SHOP", render, 16 * 2 + 10, 25 - 1, 0xDB76A5, false);

		for (int i = 0; i < 6; i++) {
			render.renderIcon(10 + i * 16, yp, Sprite.guiSelector, false, false, false);
		}
		int ammoY = 23 + 16;
		if (Player.cash >= ammo_price) {
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, ammoY, 0x363636, false);
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, (ammoY) - 1, 0xFFD400, false);
		} else {
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, ammoY, 0x191919, false);
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, (ammoY) - 1, 0x363636, false);
		}
		int trapY = 23 + 32;
		if (Player.cash >= trap_price) {
			Font.draw("Trap$" + Integer.toString(trap_price), render, 16 * 1, trapY, 0x363636, false);
			Font.draw("TRAP$" + Integer.toString(trap_price), render, 16 * 1, (trapY) - 1, 0xFFD400, false);
		} else {
			Font.draw("TRAP$" + Integer.toString(trap_price), render, 16 * 1, trapY, 0x191919, false);
			Font.draw("TRAP$" + Integer.toString(trap_price), render, 16 * 1, (trapY) - 1, 0x363636, false);
		}
		int arY = 23 + 48;
		if (Player.cash >= ar_price) {
			Font.draw("A-Rifle$" + Integer.toString(ar_price), render, 16 * 1, arY, 0x363636, false);
			Font.draw("A-Rifle$" + Integer.toString(ar_price), render, 16 * 1, (arY) - 1, 0xFFD400, false);
		} else {
			Font.draw("A-Rifle$" + Integer.toString(ar_price), render, 16 * 1, arY, 0x191919, false);
			Font.draw("A-Rifle$" + Integer.toString(ar_price), render, 16 * 1, (arY) - 1, 0x363636, false);

		}
		if (selected == 0) {
			yp = 32;
		}

		if (selected == 1) {
			yp = 48;

		}

		if (selected == 2) {
			yp = 64;
		}
	}
}
