package Terry.dev.main.gui;

import java.util.List;
import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.entity.DrawerEntity;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class CraftingMenu extends Menu {

	private String[] selections = { "return", "Volume" };
	public Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	public boolean empty = false;

	public CraftingMenu() {
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
		}

		if (input.inventory.clicked) {
			game.setMenu(null);
			Player.inventoryActivated = false;
		}
	}

	public void select(int selected) {
		if (selected == 0) volume();
		if (selected == 1) returnB();
	}

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
		{
			render.renderRect(5, 11, 6 * 16, 9 * 16, 0x6B6B6B, false);
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
		if (Player.inv_logs > 0) {
			render.renderIcon(16, 16 * 9, Sprite.logParticle, false, false, false);
			Font.draw(Integer.toString(Player.inv_logs), render, 16+20, 16 * 9 +7, 0x363636, false);
			Font.draw(Integer.toString(Player.inv_logs), render, 16+20, 16 * 9+6, 0xEFF589, false);
			
		}

		String crafting = "Crafting";
		Font.draw(crafting, render, (16 * 2) - crafting.length(), 25, 0x5E3F4E, false);
		Font.draw(crafting, render, (16 * 2) - crafting.length(), 25 - 1, 0xDB76A5, false);

		int yE = 157;
		if (empty && time % 55 < 50 / 2) {
			Font.draw("Empty!", render, 37, yE + 1, 0x7E305C, false);
			Font.draw("Empty!", render, 37, yE, 0xEF358C, false);
		} else if (empty) {
			Font.draw("Empty!", render, 37, yE + 3, 0x7E305C, false);
			Font.draw("Empty!", render, 37, yE + 2, 0xEF358C, false);
		}

		if (selected == 0) {
		}

		if (selected == 1) {
		}
	}
}
