package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class OptionsMenu extends Menu {

	private String returnB = "Return", volume = "Volume";
	private String[] selections = { "return", "Volume" };
	private int bgCol = 0;

	private Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	public OptionsMenu() {
		selected = 0;
		bgCol = 0x1b1b1b;
		sprite = Sprite.title;

	}

	public void returnB() {
		game.setMenu(new TitleMenu());
	}

	public void volume() {

	}

	public void tick() {
		time++;
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

	// selected = 0 - Play
	// selected = 1 - options
	// selected = 2 - help
	// selected = 3 - exit
	public void render(Render render) {
		int yy = 185;
		int gap = 15;
		render.renderRect(0, 0, render.width, render.height, bgCol, false);
		render.renderWH(render.width / 2 - Sprite.title.width / 2, 70, sprite, false, false, true);
		if ((anim == 0 || anim == 3)) {
			render.renderIcon(220+8, drop, Sprite.blood_drop, false, false, false);
		} else {
			render.renderIcon(220+8, 140, Sprite.blood_drop, false, false, false);
		}

		String str = volume + " " + Integer.toString((int) Game.VOL_MOD);
		if (selected == 0) {
			Font.draw(str, render, render.width / 2 - str.length() * 4, yy, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - str.length() * 4 - 10, yy, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - str.length() * 4 + str.length()*8, yy, 0xFFFFFF, false);
		} else {
			Font.draw(str, render, render.width / 2 - str.length() * 4, yy, 0xEFEFEF, false);
		}

		if (selected == 1) {
			Font.draw(returnB, render, render.width / 2 - returnB.length() * 4, yy + gap, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - returnB.length() * 4 - 10, yy+gap, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - returnB.length() * 4 + returnB.length()*8, yy+gap, 0xFFFFFF, false);
		} else {
			Font.draw(returnB, render, render.width / 2 - returnB.length() * 4, yy + gap, 0xEFEFEF, false);
		}
	}
}
