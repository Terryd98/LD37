package Terry.dev.main.gui;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.input.Input;

public class Menu {

	public Input input;
	protected Game game;
	protected int selected = 0;
	protected boolean cursorCanMoveUp = true;
	protected boolean cursorCanMoveDown = true;

	public Menu() {
	}

	public void init(Game game, Input input) {
		this.input = input;
		this.game = game;
	}

	public void tick() {

	}

	public void selector() {
		if (input.up.clicked&&cursorCanMoveUp) {
			selected--;
		}
		if (input.down.clicked&&cursorCanMoveDown) {
			selected++;

		}

		if (selected <= 0) {
			selected = 0;
		}
	}

	public void render(Render render) {
	}

}
