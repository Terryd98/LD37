package Terry.dev.main.gui;

import java.util.Random;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class HelpMenu extends Menu {

	private String returnB = "Return";

	private String help1 = "This game is in development";
	private String help2 = "by Terry D'Arcy";

	private String[] selections = { "return" };
	private int bgCol = 0;

	private Sprite sprite;
	private int time = 0;
	private int anim = 0;
	private int drop = 140;
	private Random random = new Random();

	public HelpMenu() {
		selected = 0;
		bgCol = 0x1b1b1b;
		sprite = Sprite.title;

	}

	public void returnB() {
		game.setMenu(new TitleMenu());
	}

	public void tick() {
		System.out.println(selected);
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
	}

	public void select(int selected) {
		if (selected == 0) returnB();
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

		Font.draw(help1, render, render.width / 2 - help1.length() * 4, yy, 0xEFEFEF, false);
		Font.draw(help2, render, render.width / 2 - help2.length() * 4, yy+gap, 0xEFEFEF, false);
		
		String socials = "Get in Contact: Twitter @Terryd98";
		Font.draw(socials, render, render.width/2-socials.length()*4, 255, 0xEFEFEF, false);

		Font.draw(returnB, render, render.width / 2 - returnB.length() * 4, yy + gap*2, 0xCD545E, false);
		Font.draw(">", render, render.width / 2 - returnB.length() * 4 - 10, yy+ gap*2, 0xFFFFFF, false);
		Font.draw("<", render, render.width / 2 - returnB.length() * 4 + returnB.length()*8, yy+ gap*2, 0xFFFFFF, false);
	}
}
