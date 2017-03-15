package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class CommandCentre extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	private int tickCount = 0;
	public boolean activated = false;
	private int time = 15;
	private boolean powered = false;
	private int health = 100;
	private int tick = 0;
	public boolean inRange = false;
	public boolean inAir = false;
	private Input input;
//add fuel
	public CommandCentre(double x, double y, Level level, Input input) {
		this.input = input;
		this.x = x;
		this.y = y;
		sprite = Sprite.command_centre_off;
		Game.playSound("/sounds/hit.wav", -5.0f);
	}

	public CommandCentre(Vector2i vector, Level level, Input input) {
		this.x = vector.x;
		this.y = vector.y;
		this.input = input;
		sprite = Sprite.spikes;
		Game.playSound("/sounds/hit.wav", -5.0f);
	}

	public boolean beep = false;
	public boolean pickupRange = false;

	public void tick() {
		if(level.getTile((int)x/16,(int) y/16)== Tile.wood && level.getTile((int)x/16,(int) y/16+1)== Tile.wood){
			powered = true;
		} else {
			powered = false;
		}
		
		tick++;
		if (health <= 0) {
			Game.playSound("/sounds/break.wav", -5.0f);
			level.remove(this);
		}
		List<Player> players = level.getPlayers(this, 50);

		List<Player> ps = level.getPlayers(this, 23);
		if (ps.size() > 0) {
			pickupRange = true;
		} else {
			pickupRange = false;
		}
		if (players.size() > 0&& powered) {
			inRange = true;
			if (tick % 100 < 50) {
				sprite = Sprite.command_centre_on;
				if (beep) {
					for (int i = 0; i < 1; i++) {
						beep = false;
						Game.playSound("/sounds/beep.wav", -25.0f);
						beep = false;
					}
				}
			} else {
				beep = true;
				sprite = Sprite.command_centre_flash;
			}
		} else {
			inRange = false;
			activated = false;
			sprite = Sprite.command_centre_off;
		}

		List<Zombie> zombies = level.getZombies((int) (x * 16) + 10, (int) (y * 16), 16);
		List<ChasingZombie> chaser = level.getChaserZombies((int) (x * 16) + 10, (int) (y * 16), 16);
		if (zombies.size() > 0) {
			Zombie zombie = zombies.get(0);
			zombie.speed = zombie.START_SPEED / 2;
			if (time >= 0) time--;
			if (time == 0) {
				sprite = Sprite.spikesBlood;
				health -= 5;
				time = 15;
			}
		}

		if (zombies.size() > 0) {
			Zombie zombie = zombies.get(0);
			zombie.speed = zombie.START_SPEED / 2;
			if (time >= 0) time--;
			if (time == 0) {
				sprite = Sprite.spikesBlood;
				health -= 5;
				zombie.hurt(5);
				time = 15;
			}
		}
	}

	public static boolean canAfford = false;
	public static int ar_price = 500;
	public static int ammo_price = 250;
	public static int trap_price = 50;
	public static int selectTime = 200;
	public static final int SELECT_TIME = 200;

	int yp = 16;
	int selected = 1;

	public void renderGui(Render render) {
		if (Player.cash >= ammo_price && selected == 1 && input.space && selectTime == 0 && (Player.assaultRifle || Player.pistol || Player.shotgun)) {
			Player.ASSAULT_RIFLE_AMMO = AssaultRifle.AMMO;
			Player.PISTOL_AMMO = PistolBullet.AMMO;
			Player.cash -= ammo_price;
			selectTime = SELECT_TIME;
		}
		if ((Player.ASSAULT_RIFLE_AMMO >= AssaultRifle.AMMO || Player.PISTOL_AMMO >= PistolBullet.AMMO) && selectTime == 0 && selected == 1 && input.space) {
			Game.playSound("/sounds/hit.wav", -5.0f);
			selectTime = SELECT_TIME;

		}
		
		if (Player.cash >= trap_price && selected == 2 && input.space && selectTime == 0) {
			Player.cash -= trap_price;
			Player.traps++;
			selectTime = SELECT_TIME;
		}
		if ((Player.ASSAULT_RIFLE_AMMO >= AssaultRifle.AMMO || Player.PISTOL_AMMO >= PistolBullet.AMMO) && selectTime == 0 && selected == 2 && input.space) {
			Game.playSound("/sounds/hit.wav", -5.0f);
			selectTime = SELECT_TIME;

		}

		if (Player.cash >= ar_price && selected == 3 && input.space && selectTime == 0 && !Player.assaultRifle) {
			Player.pistol = false;
			Player.assaultRifle = true;
			Player.hasAssaultRifle = true;
			Player.cash -= ar_price;
			selectTime = SELECT_TIME;
		}
		if ((Player.cash <= ar_price || Player.assaultRifle) && selectTime == 0 && selected == 3 && input.space) {
			Game.playSound("/sounds/hit.wav", -5.0f);
			selectTime = SELECT_TIME;

		}
		if (selectTime > 0) selectTime--;
		render.renderRect(5, 11, 6 * 16, 9 * 16, 0x808080);
		if (yp <= 32) yp = 32;
		if (yp >= 9 * 16) yp = 9 * 16;
		if (selected <= 1) selected = 1;
		if (selected >= 8) selected = 8;
		if (activated && input.up && selectTime == 0) {
			yp -= 16;
			selected--;
			Game.playSound("/sounds/reload.wav", -15.0f);

			selectTime = SELECT_TIME;
		}
		if (activated && input.down && selectTime == 0) {
			yp += 16;
			selected++;
			Game.playSound("/sounds/reload.wav", -15.0f);
			selectTime = SELECT_TIME;
		}

		// render.renderRect(3, yp-2, 100, 16, 0x626262);
		for (int i = 0; i < 6; i++) {
			render.renderIcon(10 + i * 16, yp, sprite.guiFull, false, false, false);
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

		Font.draw("SHOP", render, (16 * 2 + 10), 25, 0x5E3F4E, false);
		Font.draw("SHOP", render, 16 * 2 + 10, 25 - 1, 0xDB76A5, false);
		
		int ammoY= 23 + 16;
		if (Player.cash >= ammo_price) {
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, ammoY, 0x363636, false);
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, (ammoY) - 1, 0xFFD400, false);
		} else {
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, ammoY, 0x191919, false);
			Font.draw("AMMO$" + Integer.toString(ammo_price), render, 16 * 1, (ammoY) - 1, 0x363636, false);

		}

		int trapY= 23 + 32;
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
		

	}

	public void render(Render render) {
		/*
		 * List<Player> players = level.getPlayers(this, 50); if (players.size()
		 * > 0) { if (tick % 50 < 25) { render.render((int) x + 15, (int) y -
		 * 20, Sprite.f_indicator, false, false); } else { render.render((int) x
		 * + 15, (int) y - 22, Sprite.f_indicator, false, false); } }
		 */

		if (inAir) {
			render.render((int) x, (int) y + 15, Sprite.command_centre_shadow, false, false);
		}
		render.render((int) x, (int) y, sprite, false, false);
		render.render((int) x, (int) y + 16, Sprite.command_centre_legs, false, false);

	}
}
