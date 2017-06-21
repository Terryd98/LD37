package Terry.dev.main.entity.mob;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.AmmoEntity;
import Terry.dev.main.entity.CashEntity;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;

public class Boat extends Mob {

	public static double speed;
	public final double START_SPEED;
	private int time = 0;
	double xa = 0, ya = 0;
	public int health;
	public static boolean moving = false;
	public int damage = 1;
	public boolean debug = false;
	boolean knockBack = false;
	public double xDir = xa;
	public double yDir = ya;
	public static boolean onBoat = false;
	private Input input;
	private double xVel, yVel = 0.2;
	private Sprite sprite;
	private boolean inputInitiated = false;

	public Boat(Level level) {
		health = random.nextInt(40) + 20;
		findZombieStartPos(level);
		START_SPEED = 1;
		sprite = Sprite.boat;
	}

	public Boat(double x, double y, Level level) {
		this.x = x;
		this.y = y;
		health = random.nextInt(100) + 50;
		START_SPEED = 1;
		sprite = Sprite.boat;
	}

	public double playerX;
	public double playerY;
	boolean canEnter = true;

	int driftTime = 50;
	public void tick() {
		xa = 0;
		ya = 0;
		time++;
		anim++;
		List<Player> players = level.getPlayersOffseted(x + 16, y - 10, 25);
		int yz = 2;

		if (players.size() > 0 && level.getTile((int) x / 16, (int) (y / 16) + yz) == Tile.water && canEnter) {
			Player player = players.get(0);
			player.x = x;
			player.y = y;
			this.input = player.input;
			inputInitiated = true;
			
			if (!input.left.down && !input.right.down && driftTime > 0) {
				if (xVel > -0) xVel -= 0.01;
				if (xVel < -0) xVel += 0.01;
				xa -= xVel;
				if(driftTime >0) driftTime--;
			}
			if (!input.up.down && !input.down.down&& driftTime > 0) {
				if (yVel >= -0) yVel -= 0.01;
				if (yVel <= -0) yVel += 0.01;
				ya -= yVel;
				if(driftTime >0) driftTime--;
			}
			if(driftTime <= 0) {
				xVel = 0;
				yVel = 0;
			}
			System.out.println(driftTime);			
			if (input.up.down) {
				driftTime= 50;
				yVel += 0.05;
				ya -= speed * yVel;
				if (yVel >= 1) yVel = 1;
			} else if (input.down.down) {
				driftTime= 50;

				yVel += -0.05;
				ya += speed * -yVel;
				if (yVel <= -1) yVel = -1;
			}
			if (input.left.down) {
				driftTime= 50;

				xVel += 0.05;
				xa -= speed * xVel;
				if (xVel >= 1) xVel = 1;
			} else if (input.right.down) {
				driftTime= 50;

				xVel += -0.05;
				xa += speed * -xVel;
				if (xVel <= -1) xVel = -1;
			}
			System.out.println(xVel + " | " + yVel);
			onBoat = true;

			// ya += 0.1;
			// if ((int) x < (int) playerX) xa += speed * 1.2;
			// if ((int) y < (int) playerY) ya += speed * 1.2;
			// if ((int) x > (int) playerX) xa -= speed * 1.2;
			// if ((int) y > (int) playerY) ya -= speed * 1.2;

			speed = START_SPEED;
			if (input.use.clicked && onBoat && inputInitiated) {
				onBoat = false;
				canEnter = false;
			}
		} else if (players.size() < 1) {
			canEnter = true;
			onBoat = false;
		}

		if (xa != 0 || ya != 0) {
			moving = true;
			if (!debug) {
				moveBoat(xa, 0);
				moveBoat(0, ya);
			}
		} else {
			moving = false;
		}
	}

	private void attack() {
		List<Player> players = level.getPlayers(this, 16);
		if (players.size() > 0) {
			Player player = players.get(0);
			if (time % 3 == 0) {
				player.hurt(damage);
			}
		}
	}

	public void hurt(int damage) {
		health -= damage;
		if (time % 5 == 0) Game.playSound("/sounds/hurt.wav", -20.0f);
		level.add(new ParticleEmitter((int) x, (int) y, 10, 10000, level, Sprite.bloodParticle));
		if (health <= 0) {
			if (random.nextInt(3) == 0) {
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.rottenHead));
			} else if (random.nextInt(3) == 2) {
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.rottenArm));
			} else {
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.blood));
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.blood));
			}
			Player.score += 10;
			if (random.nextInt(3) < 2) {
				CashEntity cash = new CashEntity(x + random.nextInt(20), y + random.nextInt(20), level);
				level.add(cash);
			}

			if (random.nextInt(3) < 1) {
				AmmoEntity ammo = new AmmoEntity(x + random.nextInt(20), y + random.nextInt(20), level);
				level.add(ammo);
			}
			Game.ZCount--;
			level.remove(this);
		}
	}

	public void render(Render render) {
		int xx = (int) x - sprite.boat.width / 5;
		int yy = (int) y - (sprite.height / 5);
		if (anim % 50 > 25) {
			yy = (int) y - (sprite.height / 5);
			sprite = Sprite.boat1;
		} else {
			yy = (int) y - (sprite.height / 5) - 1;
			sprite = Sprite.boat;
		}

		render.renderWH(xx, yy, sprite, false, false, false);
		if (onBoat) {

			// paddle anim
			if (dir == 1 && moving) {
				if (anim % 60 >= 40) {
					render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, true, false);

				} else if (anim % 60 >= 20) {
					render.renderWH((int) x + 30, yy - 5, Sprite.paddleUD_0, false, false, false);

				} else if (anim % 60 >= 0) {
					render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, false, false);
				}
			} else if (dir == 1) {
				render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, false, false);
			}

			////////////////////////////////// PLAYER ANIMATION ON BOAT
			int yp = (int) y - Sprite.boat.height / 2;
			if (anim % 50 > 25) {
				yp = (int) y - Sprite.boat.height / 2;
			} else {
				yp = (int) y - Sprite.boat.height / 2 - 1;

			}
			if (dir == 0 && moving) {
				if (anim % 60 > 40) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight1, true, false);
				} else if (anim % 60 > 20) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight, true, false);
				} else {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight, true, false);
				}
			} else if (dir == 0) {
				render.renderPlayer((int) x + 18, (int) yp, Sprite.playerPaddleRight, true, false);
			}
			if (dir == 1 && moving) {
				if (anim % 60 > 40) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleUp, false, false);
				} else if (anim % 60 > 20) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleUp1, false, false);
				} else {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleUp, false, false);

				}
			} else if (dir == 1) {
				render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleUp, false, false);
			}

			if (dir == 2 && moving) {
				if (anim % 60 > 40) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight1, false, false);
				} else if (anim % 60 > 20) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight, false, false);
				} else {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight, false, false);
				}
			} else if (dir == 2) {
				render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleRight, false, false);
			}
			if (dir == 3 && moving) {
				if (anim % 60 > 40) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleDown, false, false);
				} else if (anim % 60 > 20) {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleDown1, false, false);
				} else {
					render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleDown, false, false);

				}
			} else if (dir == 3) {
				render.renderPlayer((int) x + 18, yp, Sprite.playerPaddleDown, false, false);
			}

			////////////////////////////////////////////////////////// PADDLE
			////////////////////////////////////////////////////////// ANIMATION
			if (dir == 0 && moving) {
				if (anim % 60 >= 40) {
					render.renderWH((int) x + 15, yy, Sprite.paddleLR_0, false, false, false);

				} else if (anim % 60 >= 20) {
					render.renderWH((int) x + 15, yy + 5, Sprite.paddleLR_1, false, false, false);

				} else if (anim % 60 >= 0) {
					render.renderWH((int) x + 15, yy, Sprite.paddleLR_0, true, false, false);
				}
			} else if (dir == 0) {
				render.renderWH((int) x + 15, yy, Sprite.paddleLR_0, true, false, false);
			}

			if (dir == 2 && moving) {
				if (anim % 60 >= 40) {
					render.renderWH((int) x + 21, yy, Sprite.paddleLR_0, true, false, false);

				} else if (anim % 60 >= 20) {
					render.renderWH((int) x + 21, yy + 5, Sprite.paddleLR_1, false, false, false);

				} else if (anim % 60 >= 0) {
					render.renderWH((int) x + 21, yy, Sprite.paddleLR_0, false, false, false);
				}
			} else if (dir == 2) {
				render.renderWH((int) x + 21, yy, Sprite.paddleLR_0, true, false, false);
			}
			// paddle anim
			if (dir == 3 && moving) {
				if (anim % 60 >= 40) {
					render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, false, false);

				} else if (anim % 60 >= 20) {
					render.renderWH((int) x + 30, yy - 5, Sprite.paddleUD_0, false, false, false);

				} else if (anim % 60 >= 0) {
					render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, true, false);
				}
			} else if (dir == 3) {
				render.renderWH((int) x + 25, yy - 5, Sprite.paddleUD_1, false, false, false);
			}

		}
	}
}
