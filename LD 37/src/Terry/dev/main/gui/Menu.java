package Terry.dev.main.gui;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.input.Input;

public class Menu {

	public Input input;
	protected Game game;

	public Menu() {
	}

	public void init(Game game, Input input) {
		this.input = input;
		this.game = game;
	}

	public void tick() {
	}

	public void render(Render render) {
	}

}
