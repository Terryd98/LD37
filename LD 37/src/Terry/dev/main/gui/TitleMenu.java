package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class TitleMenu extends Menu {

	private String play = "Play", options = "Options", help = "Help", exit = "Exit to Desktop";
	private String[] selections = { "play", "options", "help", "exit" };
	private int bgCol = 0;
	private Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	public TitleMenu() {
		bgCol = 0x1b1b1b;
		sprite = Sprite.title;
		selected = 0;
	}

	public void play() {
		game.setMenu(null);
	}

	public void options() {
		game.setMenu(null);
		game.setMenu(new OptionsMenu());
	}

	public void help() {
		game.setMenu(null);
		game.setMenu(new HelpMenu());
	}

	public void exit() {
		System.exit(0);
	}

	public void tick() {
		game.requestFocus();

		time++;
		if (selected >= selections.length - 1) selected = selections.length - 1;

		selector();
		if (input.space.clicked ) select(selected);

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
		if (selected == 0) play();
		if (selected == 1) options();
		if (selected == 2) help();
		if (selected == 3) exit();
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
		if (selected == 0) {
			Font.draw(play, render, render.width / 2 - play.length() * 4, yy, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - play.length() * 4 - 10, yy, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - play.length() * 4 + play.length() * 8, yy, 0xFFFFFF, false);
		} else {
			Font.draw(play, render, render.width / 2 - play.length() * 4, yy, 0xEFEFEF, false);
		}

		if (selected == 1) {
			Font.draw(options, render, render.width / 2 - options.length() * 4, yy + gap, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - options.length() * 4 - 10, yy+ gap, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - options.length() * 4 + options.length()*8, yy+ gap, 0xFFFFFF, false);
		} else {
			Font.draw(options, render, render.width / 2 - options.length() * 4, yy + gap, 0xEFEFEF, false);
		}

		if (selected == 2) {
			Font.draw(help, render, render.width / 2 - help.length() * 4, yy + gap * 2, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - help.length() * 4 - 10, yy+ gap * 2, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - help.length() * 4 + help.length()*8, yy+ gap * 2, 0xFFFFFF, false);
		} else {
			Font.draw(help, render, render.width / 2 - help.length() * 4, yy + gap * 2, 0xEFEFEF, false);
		}

		if (selected == 3) {
			Font.draw(exit, render, render.width / 2 - exit.length() * 4, yy + gap * 3, 0xCD545E, false);
			Font.draw(">", render, render.width / 2 - exit.length() * 4 - 10, yy+ gap * 3, 0xFFFFFF, false);
			Font.draw("<", render, render.width / 2 - exit.length() * 4 + exit.length()*8, yy+ gap * 3, 0xFFFFFF, false);
		} else {
			Font.draw(exit, render, render.width / 2 - exit.length() * 4, yy + gap * 3, 0xEFEFEF, false);
		}
	}

}
