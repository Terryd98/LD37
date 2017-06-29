package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.entity.CommandCentre;
import Terry.dev.main.entity.WorkTableEntity;
import Terry.dev.main.entity.mob.Boat;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class CraftingMenu extends Menu {

	private String[] selections = { "test1", "test2", "test3" };
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
		if (WorkTableEntity.inRange && input.use.clicked) {
			game.setMenu(null);
			WorkTableEntity.activated = false;
		}

		time++;
		if (selected >= selections.length - 1) selected = selections.length - 1;

		selector();
		if (input.space.clicked) {
			select(selected);
		}

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

	}

	boolean canCraftRaft = false;

	public void select(int selected) {
		if (selected == 0 && canCraftRaft) craftRaft();
		if (selected == 1) returnB();
	}

	private void craftRaft() {
		Player.inv_logs -= raftLogs;
		Player.craftRaft = true;
	}

	public static final int SELECT_TIME = 200;

	int yp = 16;

	// selected = 0 - Play
	// selected = 1 - options
	// selected = 2 - help
	// selected = 3 - exit

	public void render(Render render) {
		int yy = 185;
		int gap = 15;
		{
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
		}
		for (int i = 0; i < 6; i++) {
			render.renderIcon(10 + i * 16, yp, Sprite.guiSelector, false, false, false);
		}

		String raft = "Raft";
		int ya = 22 + 16;
		Font.draw(raft, render, 16 * 1, ya, 0x363636, false);
		Font.draw(raft, render, 16 * 1, ya - 1, 0xFFD400, false);

		String test1 = "test1";
		Font.draw(test1, render, 16 * 1, ya + 16, 0x363636, false);
		Font.draw(test1, render, 16 * 1, ya + 16 - 1, 0xFFD400, false);

		String test2 = "test2";
		Font.draw(test2, render, 16 * 1, ya + 32, 0x363636, false);
		Font.draw(test2, render, 16 * 1, ya + 32 - 1, 0xFFD400, false);

		String crafting = "Crafting";
		Font.draw(crafting, render, (16 * 2) - crafting.length(), 25, 0x5E3F4E, false);
		Font.draw(crafting, render, (16 * 2) - crafting.length(), 25 - 1, 0xDB76A5, false);

		if (selected == 0) {
			yp = 32;
			showRaftRecipe(render, ya);
		}

		if (selected == 1) {
			yp = 48;

		}

		if (selected == 2) {
			yp = 64;
		}
	}

	int raftLogs = 5;

	private void showRaftRecipe(Render render, int y) {
		render.renderIcon(16 * 7 - 1, y - 5, Sprite.guiSide, true, true, false);
		render.renderIcon(16 * 7 + 4, y - 5, Sprite.guiSide, true, true, false);
		render.renderIcon(16 * 7 + 9, y - 5, Sprite.guiSide, true, true, false);
		render.renderIcon(16 * 7 + 14, y - 5, Sprite.guiSide, true, true, false);
		render.renderIcon(16 * 7 + 17, y - 5, Sprite.guiSide, true, true, false);
		render.renderIcon(16 * 7 - 4, y - 5, sprite.logParticle, false, false, false);

		if (Player.inv_logs >= raftLogs) {
			canCraftRaft = true;
			Font.draw(Integer.toString(raftLogs), render, 16 * 7, y - 3, 0xFFD400, false);
		} else {
			canCraftRaft = false;
			Font.draw(Integer.toString(raftLogs), render, 16 * 7, y - 3, 0x363636, false);

		}

		Font.draw("have", render, 16 * 7 + 5, y - 12, 0xDB76A5, false);
		Font.draw(Integer.toString(Player.inv_logs), render, 16 * 7 + 15, y - 3, 0xDB76A5, false);

	}
}
