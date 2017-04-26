package Terry.dev.main.gui;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.input.Input;

public class Menu {

	private String play = "Play", options = "Options", help = "Help", exit = "Exit to Desktop";
	private int selected = 0;
	private int bgCol = 0;
	private Input input;
	private int st = 10;
	private final int ST = st;
	private boolean enabled = true;

	public Menu(Input input) {
		selected = 0;
		bgCol = 0x2b2b2b;
		Game.menu = true;
		this.input = input;
	}

	public void play() {
		Game.menu = false;
		enabled = false;
	}

	public void options() {

	}

	public void help() {

	}

	public void exit() {
		System.exit(0);
	}

	public void tick() {
		System.out.println(st);
		if (st >= 1)
			st--;
		if (selected >= 3)
			selected = 3;
		if (selected <= 0)
			selected = 0;

		if (input.up && st == 0) {
			selected--;
			st = ST;
		}
		if (input.down && st == 0) {
			selected++;
			st = ST;
		}

		if (input.space)
			select(selected);

	}

	public void select(int selected) {
		if (selected == 0)
			play();
		if (selected == 1)
			options();
		if (selected == 2)
			help();
		if (selected == 3)
			exit();
	}

	// selected = 0 - Play
	// selected = 1 - options
	// selected = 2 - help
	// selected = 3 - exit
	public void render(Render render) {
		if (enabled) {
			
			int yy = 150;
			render.renderRect(0, 0, render.width, render.height, bgCol, false);
			Font.draw("Project Z", render, render.width/2, 30, 0xff00ff, false);
			if (selected == 0) {
				Font.draw(play, render, render.width / 2 - play.length() * 4, yy, 0xffffff, false);
			} else {
				Font.draw(play, render, render.width / 2 - play.length() * 4, yy, 0x7b7b7b, false);
			}
			if (selected == 1) {
				Font.draw(options, render, render.width / 2 - options.length() * 4, yy + 10, 0xffffff, false);
			} else {
				Font.draw(options, render, render.width / 2 - options.length() * 4, yy + 10, 0x7b7b7b, false);
			}
			if (selected == 2) {
				Font.draw(help, render, render.width / 2 - help.length() * 4, yy + 20, 0xffffff, false);
			} else {
				Font.draw(help, render, render.width / 2 - help.length() * 4, yy + 20, 0x7b7b7b, false);
			}
			if (selected == 3) {
				Font.draw(exit, render, render.width / 2 - exit.length() * 4, yy + 30, 0xffffff, false);
			} else {
				Font.draw(exit, render, render.width / 2 - exit.length() * 4, yy + 30, 0x7b7b7b, false);
			}
		}
	}

}
