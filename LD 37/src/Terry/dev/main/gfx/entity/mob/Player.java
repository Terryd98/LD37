package Terry.dev.main.gfx.entity.mob;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.gfx.entity.DefaultGun;
import Terry.dev.main.gfx.entity.Projectile;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.OneLevel;

public class Player extends Mob {

	public int health = 100;
	public boolean dead = false;
	public double speed = 1.2;
	public static int score = 0;
	private int time = 0;
	private static int AMMO = DefaultGun.AMMO;
	public static int CLIPS = DefaultGun.CLIP;

	Projectile p;
	private int fireRate = 0;

	public Player(Input input, Level level) {
		this.input = input;
		findStartPos(level);
		fireRate = DefaultGun.FIRERATE;
	}

	public void tick() {
		anim++;
		if (!Game.menu) {
			time++;
		}
		if (fireRate > 0) fireRate--;
		int xa = 0, ya = 0;
		if (!Game.menu) {
			if (input.up) ya -= speed;
			else if (input.down) ya += speed;
			if (input.left) xa -= speed;
			else if (input.right) xa += speed;
		}

		if (xa != 0 || ya != 0) {
			walking = true;
			move(xa, 0);
			move(0, ya);
		} else {
			walking = false;
		}
		tickShots();
		clear();
		if (walking && anim % 15 == 0) Game.playSound("/sounds/hit.wav", -25.0f);
	}

	public boolean reload = false;

	private void tickShots() {
		if (AMMO >= 20) {
			reload = false;
			AMMO = 20;
		}

		if (input.reload && AMMO != DefaultGun.AMMO) {
			reload = true;
		}
		if (CLIPS <= 0) CLIPS = 0;
		if (CLIPS > 0 && reload) {
			if (time % 20 == 0) {
				AMMO++;
				CLIPS--;
				Game.playSound("/sounds/reload.wav", -10.0f);
			}
		}
		if (Input.getButton() == 1 && AMMO == 0) {
			if (time % 5 == 0) Game.playSound("/sounds/outOfAmmo.wav", -10.0f);
		}
		if (!Game.menu) {

			if (Input.getButton() == 1 && fireRate == 0) {
				reload = false;
				//AMMO--;
				if (AMMO <= 0) {
					AMMO = 0;
				}
				if (AMMO > 0) {
					double dx = (Input.getX() - (Game.getWWidth() / 2)) - 0;
					double dy = (Input.getY() - (Game.getWHeight() / 2)) - 0;
					double direction = Math.atan2(dy, dx);
					if (dir == 0) {
						shoot(x - 12, y, direction);
					}
					if (dir == 1) {
						shoot(x - 7, y - 18, direction);
					}
					if (dir == 2) {
						shoot(x - 4, y, direction);
					}
					if (dir == 3) {
						shoot(x - 7, y, direction);
					}
					fireRate = DefaultGun.FIRERATE;
				}
			}
		}
	}

	public void hurt(int damage) {
		health -= damage;
		if (time % 40 == 0) {
			Game.playSound("/sounds/zombie.wav", -10.0f);
		}
		if (health <= 0) {
			Game.playSound("/sounds/death.wav", -20.0f);
			level.remove(this);
			dead = true;
		}
	}

	private int finalMessageTime = 0;

	public void render(Render render) {
		int xx = (int) Math.abs(x);
		int yy = (int) Math.abs(y);
		if (Game.firstSpawn) {
			if (time < 200) {
				String msg = "They are Coming!";
				int xxx = xx - msg.length() * 4;
				int yyy = yy - 50;
				Font.draw(msg, render, xxx + 1, yyy + 1, 0x592828, true);
				Font.draw(msg, render, xxx, yyy, 0xCC5656, true);
			} else {
				Game.firstSpawn = false;
			}
		}

		render.render(xx - 8, yy + 5, Sprite.shadow, false, false);
		render.renderRect(0, 0, Game.getWWidth(), 12, 0x808080);
		if (AMMO > 0) {

			Font.draw(Integer.toString(AMMO), render, (render.width - 17), 3, 0x7E305C, false);
			Font.draw(Integer.toString(AMMO), render, (render.width - 17), 2, 0xEF358C, false);
		}

		if (AMMO <= 0) {
			if (time % 40 > 20) {
				Font.draw(Integer.toString(AMMO), render, (render.width - 17), 3, 0x7E305C, false);
				Font.draw(Integer.toString(AMMO), render, (render.width - 17), 2, 0xEF358C, false);
			} else {
				Font.draw(Integer.toString(AMMO), render, (render.width - 17), 3, 0x5C5E38, false);
				Font.draw(Integer.toString(AMMO), render, (render.width - 17), 2, 0xB1B564, false);
			}
		}

		if (CLIPS <= 0) {
			if (time % 40 > 20) {
				Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
				Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
			} else {
				Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 3, 0x5C5E38, false);
				Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 2, 0xB1B564, false);
			}
		}

		if (!dead && Game.finalLevel) {
			finalMessageTime++;
			if (finalMessageTime < 10000) {

				String msg = "She is dead! aaaand so am i!";
				Font.draw(msg, render, xx - msg.length() * 4, yy - 30, 0x7E305C, true);
			}
		}

		if (!dead && Game.infiniLevel) {
			finalMessageTime++;
			if (finalMessageTime < 10000) {
				String msg = "you survived! thats surp..ehhmazing!";
				String msg2 = "see how high you can get your score!";
				Font.draw(msg, render, xx - msg.length() * 4, yy - 40, 0x7E305C, true);
				Font.draw(msg2, render, xx - msg.length() * 4 + 2, yy - 30, 0x7E305C, true);
			}
		}

		Font.draw("Ammo:", render, (render.width - 20) - 65, 3, 0x363636, false);
		Font.draw("Ammo:", render, (render.width - 20) - 65, 2, 0xEFF589, false);

		if (CLIPS > 0) {
			Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
			Font.draw(Integer.toString(CLIPS) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
		}

		Font.draw("Health:", render, 3, 3, 0x363636, false);
		Font.draw("Health:", render, 2, 2, 0xEFF589, false);

		Font.draw(Integer.toString(health), render, 3 + 57, 3, 0x363636, false);
		Font.draw(Integer.toString(health), render, 2 + 57, 2, 0xEF358C, false);

		if (dir == 1) {
			if (walking && anim % 20 > 10) {
				render.render(xx - 16, yy - 16, Sprite.playerUp1, false, false);
			} else if (walking) {
				render.render(xx - 16, yy - 16, Sprite.playerUp2, false, false);
			} else {
				render.render(xx - 16, yy - 16, Sprite.playerStillUp, false, false);
			}
		}

		if (dir == 3) {
			if (walking && anim % 20 > 10) {
				render.render(xx - 16, yy - 16, Sprite.playerDown1, false, false);
			} else if (walking) {
				render.render(xx - 16, yy - 16, Sprite.playerDown2, false, false);
			} else {
				render.render(xx - 16, yy - 16, Sprite.playerStillDown, false, false);

			}
		}

		if (dir == 2) {
			if (walking && anim % 20 > 10) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerRight2, false, false);
			} else if (walking && anim % 20 > 3) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
			} else if (walking) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerRight1, false, false);
			} else {
				render.render((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
			}
		}

		if (dir == 0) {
			if (walking && anim % 20 > 10) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerRight2, true, false);
			} else if (walking && anim % 20 > 3) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
			} else if (walking) {
				render.render((int) x - 16, (int) y - 16, Sprite.playerRight1, true, false);
			} else {
				render.render((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
			}
		}

	}

}
