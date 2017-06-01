package Terry.dev.main.entity.mob;

import Terry.dev.main.Game;
import Terry.dev.main.SaveGame;
import Terry.dev.main.entity.CommandCentre;
import Terry.dev.main.entity.DrawerEntity;
import Terry.dev.main.entity.GrenadeEntity;
import Terry.dev.main.entity.Trap;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.entity.gun.AssaultRifle;
import Terry.dev.main.entity.gun.PistolBullet;
import Terry.dev.main.entity.gun.Projectile;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class Player extends Mob {

	public int health = 100;
	public static int playerDir;
	public static boolean dead = false;
	public double speed = WALKING_SPEED;
	public static int energy = 100;
	public static int cash = 0;
	public static int addedCash = 0;
	public static int addedAmmo = 0;
	public int tickCount = 0;
	private static boolean armed = true;
	private static final double WALKING_SPEED = 1;
	private static final double RUNNING_SPEED = 1.5;
	public static int score = 0;
	public boolean carrying = false;
	private int time = 0;
	private Trap trap;
	public CommandCentre cCentre;
	private GrenadeEntity grenade;
	public int placeDir = 0;
	public static int traps = 0;
	private final int SHAKE_TIME = 10;
	private int shakeTime = SHAKE_TIME;
	Projectile p;
	public static int cashPickupTime = 50;
	public static int ammoPickupTime = 50;
	public static boolean pistol = true;
	public static boolean shotgun = false;
	public static boolean assaultRifle = false;
	public static boolean hasPistol = true;
	public static boolean hasShotgun = false;
	public static boolean hasAssaultRifle = false;
	public static int PISTOL_CLIP = PistolBullet.CLIP;
	public static int PISTOL_AMMO = PistolBullet.AMMO;
	public int pistol_fireRate = PistolBullet.FIRERATE;
	public int ASSAULT_RIFLE_CLIP = AssaultRifle.CLIP;
	public static int ASSAULT_RIFLE_AMMO = AssaultRifle.AMMO;
	public static boolean hasKey = false;
	public int ASSAULT_RIFLE_fireRate = AssaultRifle.FIRERATE;
	public int finalMessageTime = 0;
	public boolean trapToggled = false;
	public boolean reload = false;
	public boolean shooting = false;
	public int reloadTime = 100;
	public final int RELOAD_TIME = 100;
	private boolean swimming = false;
	public boolean flashLight = false;

	public Player(Input input, Level level) {
		this.input = input;
		// findStartPos(level);
		x = SaveGame.read(0);
		y = SaveGame.read(1);
		cCentre = new CommandCentre((int) x + 8, (int) y, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x, y, level);
		level.add(drawer);
		level.add(new Boat((int) x, (int) y - 100, level));
	}

	public Player(int x, int y, Input input, Level level) {
		this.x = x;
		this.y = y;
		this.input = input;
		cCentre = new CommandCentre((int) x + 8, (int) y, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x, y, level);
		level.add(drawer);

	}

	public Player(Vector2i vector, Input input, Level level) {
		this.input = input;
		this.x = vector.x;
		this.y = vector.y;
		vector.x += 8;
		cCentre = new CommandCentre(vector, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x - 2 * 16, y - 2 * 16, level);
		level.add(drawer);

	}

	double dVelocityX = 0.0;
	double dVelocityY = 15.0;
	double dFriction = 0.90; // simple friction coefficient, 1.0 means no
								// friction

	int xStart = 10;
	int yStart = 10;
	double yVel = 0.2;
	double xVel = 0.2;

	public void tick() {

		if (input.t.clicked && !flashLight) {
			flashLight = true;
		} else if (input.t.clicked && flashLight) {
			flashLight = false;
		}

		if (DrawerEntity.inRange && input.use.clicked && !trapToggled) {
			System.out.println(22);
			DrawerEntity.looting = true;
		}
		if (cCentre.inRange && input.use.clicked && !cCentre.activated && !trapToggled && !DrawerEntity.looting) {
			cCentre.activated = true;
		}
		if (input.up.down && input.down.down && input.shift.down) cash++;

		if (hasPistol && input.one.clicked) {
			assaultRifle = false;
			shotgun = false;
			pistol = true;
		} else if (hasAssaultRifle && input.two.clicked) {
			assaultRifle = true;
			shotgun = false;
			pistol = false;

		}

		if (!trapToggled && input.space.down && energy >= 2 && cCentre.pickupRange && !cCentre.activated && !DrawerEntity.looting) {
			carrying = true;
			armed = false;
			if (anim % 10 == 0) energy--;

			cCentre.inAir = true;
		} else {
			armed = true;
			carrying = false;
			cCentre.inAir = false;
		}

		if (level.getTile((int) (x) / 16, (int) (y + 16) / 16) == Tile.flower) {
			level.setTile((int) x / 16, (int) ((y + 16) / 16), Tile.grass);
			Game.playSound("/sounds/flower.wav", -10.0f);
		}
		if (input.upArrow.clicked) placeDir = 1;
		if (input.downArrow.clicked) placeDir = 3;
		if (input.leftArrow.clicked) placeDir = 0;
		if (input.rightArrow.clicked) placeDir = 2;
		if (trapToggled || carrying || cCentre.activated || swimming || hasKey) {
			armed = false;
		} else {
			armed = true;
		}

		if (input.trap.clicked && !cCentre.activated && !DrawerEntity.looting && !trapToggled) {
			trapToggled = true;
		} else if (trapToggled && input.trap.clicked) {
			trapToggled = false;
		}
		if (input.space.clicked && trapToggled && !carrying) {
			place();
		}

		if (!walking && !running) {
			still = true;
		} else {
			still = false;
		}
		anim++;
		time++;
		if (energy <= 0) energy = 0;
		if (energy >= 100) energy = 100;

		if (energy < 100 && ((!running && walking) || (!running && !walking)) && !carrying && anim % 15 == 0) {
			if (still) energy += 5;

			energy++;
		}

		if (!carrying && !swimming && input.shift.down && energy > 0) {
			speed = RUNNING_SPEED;
			if (running && anim % 5 == 0) energy--;
		} else {
			speed = WALKING_SPEED;
		}

		double xa = 0, ya = 0;
		if (input.control.down) speed /= 2;

		if (Boat.onBoat) {
			swimming = false;
			carrying = false;
			armed = false;
		}

		if (level.getTile((int) x / 16, (int) (y / 16) + 1) == Tile.water) {
			swimming = true;
			speed /= 2;
		} else {
			swimming = false;
		}

		if (!cCentre.activated && !DrawerEntity.looting) {
			if (carrying) {
				running = false;
				walking = true;
				speed = 0.7;
			}

			if (!input.up.down && !input.down.down) {
				yVel = 0;
			}
			if (!input.left.down && !input.right.down) {
				xVel = 0;
			}

			// System.out.println("yVel = " + yVel + " xVel = " + xVel);
			if (input.up.down) {
				yVel += 0.2;
				ya -= speed * yVel;
				if (yVel >= 1.2) yVel = 1.2;
			} else if (input.down.down) {
				yVel += -0.2;
				ya += speed * -yVel;
				if (yVel <= -1.2) yVel = -1.2;
			}
			if (input.left.down) {
				xVel += 0.2;
				xa -= speed * xVel;
				if (xVel >= 1.2) xVel = 1.2;
			} else if (input.right.down) {
				xVel += -0.2;
				xa += speed * -xVel;
				if (xVel <= -1.2) xVel = -1.2;
			}
		}
		
		if (level.getTile((int) x / 16, (int) y / 16) == Tile.water) {
			carrying = false;
		}
 		if (xa < 0) playerDir = 0;
		if (xa > 0) playerDir = 2;
		if (ya < 0) playerDir = 1;
		if (ya > 0) playerDir = 3;
		// TODO: Temp
		if (input.mouseB == 3 && pistol_fireRate == 0) {
			grenade = new GrenadeEntity(x, y, level);
			level.add(grenade);
			pistol_fireRate = PistolBullet.FIRERATE;

		}
		if (xa != 0 || ya != 0) {
			if (speed == WALKING_SPEED || speed == WALKING_SPEED / 2) {
				walking = true;
				running = false;
			}
			if (speed == RUNNING_SPEED) {
				running = true;
				walking = false;
			}
			move2(xa, 0);
			move2(0, ya);
		} else {
			running = walking = false;
		}
		tickShots();
		clear();
		if (walking | running && anim % 15 == 0) {
			Game.playSound("/sounds/hit.wav", -55.0f);
		}

		if (running || shooting || GrenadeEntity.exploding && shakeTime > 0) {
			shakeTime--;
			/*
			 * if (running) { Game.shake = 2; } else
			 */if (assaultRifle && shooting) {
				Game.shake = 6;
			} else if (pistol && shooting) {
				Game.shake = 4;
			} else if (GrenadeEntity.exploding) {
				Game.shake = 10;
			} else {
				Game.shake = 0;
			}
		} else {
			shakeTime = SHAKE_TIME;
			Game.shake = 0;
			shooting = false;
		}
	}

	private void tickShots() {
		if (pistol) {
			if (pistol_fireRate > 0) pistol_fireRate--;
			if (PISTOL_CLIP >= 20) {
				reload = false;
				PISTOL_CLIP = 20;
			}
			if (input.reload.clicked && PISTOL_CLIP != PistolBullet.CLIP) {
				reload = true;
			}
			if (PISTOL_AMMO <= 0) PISTOL_AMMO = 0;
			if (PISTOL_AMMO > 0 && reload) {
				if (time % 20 == 0) {
					PISTOL_CLIP++;
					PISTOL_AMMO--;
					Game.playSound("/sounds/reload.wav", -10.0f);
				}
			}
			if (Input.getButton() == 1 && PISTOL_CLIP == 0) {
				if (time % 5 == 0) Game.playSound("/sounds/outOfAmmo.wav", -10.0f);
			}
			if (Input.getButton() == 1 && pistol_fireRate == 0 && !trapToggled && armed) {
				shooting = true;
				reload = false;
				if (PISTOL_CLIP <= 0) {
					PISTOL_CLIP = 0;
					shooting = false;
				}
				if (PISTOL_CLIP > 0) {
					double dx = (Input.getX() - (Game.getWWidth() / 2)) - 0;
					double dy = (Input.getY() - (Game.getWHeight() / 2)) - 0;
					double direction = Math.atan2(dy, dx);
					if (playerDir == 0) {
						shoot(x - 7, y + 4, direction, 1);
						PISTOL_CLIP--;
					}
					if (playerDir == 1) {
						shoot(x - 7, y + 4, direction, 1);
						PISTOL_CLIP--;

					}
					if (playerDir == 2) {
						shoot(x - 6, y + 4, direction, 1);
						PISTOL_CLIP--;

					}
					if (playerDir == 3) {
						shoot(x - 7, y, direction, 1);
						PISTOL_CLIP--;

					}
					level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.casingParticle));
					pistol_fireRate = PistolBullet.FIRERATE;
				}
			}
		}

		if (Input.getButton() == -1) shooting = false;

		if (assaultRifle) {
			if (ASSAULT_RIFLE_fireRate > 0) ASSAULT_RIFLE_fireRate--;
			if (ASSAULT_RIFLE_CLIP >= AssaultRifle.CLIP) {
				reload = false;
				ASSAULT_RIFLE_CLIP = AssaultRifle.CLIP;
			}
			if (input.reload.clicked && ASSAULT_RIFLE_CLIP != AssaultRifle.CLIP) {
				reload = true;
			}
			if (ASSAULT_RIFLE_AMMO <= 0) ASSAULT_RIFLE_AMMO = 0;

			if (ASSAULT_RIFLE_AMMO > 0 && reload) {
				reloadTime--;
				if (time % 50 == 0) Game.playSound("/sounds/reload.wav", -10.0f);

				if (reloadTime <= 0) reloadTime = 0;
				if (reloadTime == 0) {
					ASSAULT_RIFLE_CLIP += AssaultRifle.CLIP;
					ASSAULT_RIFLE_AMMO -= AssaultRifle.CLIP;
					reloadTime = RELOAD_TIME;
				}

			}

			if (Input.getButton() == 1 && ASSAULT_RIFLE_CLIP == 0) {
				if (time % 5 == 0) Game.playSound("/sounds/outOfAmmo.wav", -10.0f);
			}

			if (Input.getButton() == 1 && ASSAULT_RIFLE_fireRate == 0 && !trapToggled && armed) {
				shooting = true;
				reload = false;
				if (ASSAULT_RIFLE_CLIP <= 0) {
					ASSAULT_RIFLE_CLIP = 0;
					shooting = false;
				}
				if (ASSAULT_RIFLE_CLIP > 0) {
					double dx = (Input.getX() - (Game.getWWidth() / 2)) - 0;
					double dy = (Input.getY() - (Game.getWHeight() / 2)) - 0;
					double direction = Math.atan2(dy, dx);
					if (playerDir == 0) {
						shoot(x - 7, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;
					}
					if (playerDir == 1) {
						shoot(x - 7, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;

					}
					if (playerDir == 2) {
						shoot(x - 6, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;

					}
					if (playerDir == 3) {
						shoot(x - 7, y, direction, 3);
						ASSAULT_RIFLE_CLIP--;

					}
					level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.casingParticle));
					ASSAULT_RIFLE_fireRate = AssaultRifle.FIRERATE;
				}
			}
		}
	}

	public void hurt(int damage) {
		health -= damage;
		level.add(new ParticleEmitter((int) x, (int) y, 10, 100000, level, Sprite.bloodParticle));
		if (time % 5 == 0) Game.playSound("/sounds/hit.wav", -10.0f);

		if (time % 40 == 0) {
			Game.playSound("/sounds/zombie.wav", -10.0f);
		}
		if (health <= 0) {
			Game.playSound("/sounds/death.wav", -20.0f);
			level.remove(this);
			dead = true;
		}
	}

	public void place() {
		if (traps >= 1 && placeDir == 0) {
			int xx = (int) ((x / 16) - 2);
			int yy = (int) (y / 16);
			if (!level.getTile(xx, yy).solid()) {
				trap = new Trap(xx, yy, level);

				level.add(trap);
				traps--;
			}
		}
		if (traps >= 1 && placeDir == 1) {
			int xx = (int) (x / 16);
			int yy = (int) (y / 16) - 1;
			if (!level.getTile(xx, yy).solid()) {
				trap = new Trap(xx, yy, level);
				level.add(trap);
				traps--;
			}
		}
		if (traps >= 1 && placeDir == 2) {
			int xx = (int) (x / 16) + 2;
			int yy = (int) (y / 16);
			if (!level.getTile(xx, yy).solid()) {
				trap = new Trap(xx, yy, level);
				level.add(trap);
				traps--;
			}
		}
		if (traps >= 1 && placeDir == 3) {
			int xx = (int) (x / 16);
			int yy = (int) (y / 16) + 2;
			if (!level.getTile(xx, yy).solid()) {
				trap = new Trap(xx, yy, level);
				level.add(trap);
				traps--;
			}
		}
	}

	public static void addCash(int amount) {
		Game.playSound("/sounds/Cash.wav", -15.0f);
		addedCash += amount;
		cash += amount;
		cashPickupTime = 40;
		Game.cashY = 0;
	}

	public static void addAmmo(int amount) {
		Game.playSound("/sounds/reload.wav", -15.0f);
		if (assaultRifle) Player.ASSAULT_RIFLE_AMMO += amount;
		if (pistol) Player.PISTOL_AMMO += amount;
		Player.addedAmmo += amount;
		ammoPickupTime = 40;
		Game.ammoY = 0;
	}

	public void render(Render render) {
		int xx = (int) Math.abs(x);
		int yy = (int) Math.abs(y);
		if (Game.firstSpawn) {
			if (time < 200) {
				String msg = "They are Coming!";
				int xxx = xx - msg.length() * 4;
				int yyy = yy - 50;
				// Font.draw(msg, render, xxx + 1, yyy + 1, 0x592828, true);
				// Font.draw(msg, render, xxx, yyy, 0xCC5656, true);
			} else {
				Game.firstSpawn = false;
			}
		}
		if (trapToggled || cCentre.activated) {

			render.renderIcon(5, render.height - 20, Sprite.spikeIcon, false, false, false);
			String msg = Integer.toString(traps);
			Font.draw(msg, render, 25, render.height - 15, 0x363636, false);
			Font.draw(msg, render, 25, render.height - 16, 0xEF358C, false);
		}
		if (placeDir == 0) {
			if (trapToggled) {
				int xs = (int) (x / 16) - 2;
				int ys = (int) (y / 16);
				render.render(xs * 16, ys * 16, Sprite.selector, false, false);
			}
		}

		if (placeDir == 1) {
			if (trapToggled) {
				int xs = (int) (x / 16);
				int ys = (int) (y / 16) - 1;

				render.render(xs * 16, ys * 16, Sprite.selector, false, false);
			}
		}
		if (placeDir == 2) {
			if (trapToggled) {
				int xs = (int) (x / 16) + 2;
				int ys = (int) (y / 16);
				render.render(xs * 16, ys * 16, Sprite.selector, false, false);
			}
		}

		if (placeDir == 3) {
			if (trapToggled) {
				int xs = (int) (x / 16);
				int ys = (int) (y / 16) + 2;
				render.render(xs * 16, ys * 16, Sprite.selector, false, false);
			}
		}

		/////////////////////////////////////////////////////////// PLAYER ANIM
		if (!swimming) {
			if (playerDir == 1) {
				render.render(xx - 8, yy + 6, Sprite.shadow, false, false);
			}
			if (playerDir == 3) {
				render.render(xx - 8, yy + 6, Sprite.shadow, false, true);
			}
			if (playerDir == 0) {
				render.render(xx - 11, yy + 7, Sprite.shadow, false, false);
			}
			if (playerDir == 2) {
				render.render(xx - 5, yy + 7, Sprite.shadow, false, false);
			}
		}
		///////////////////////////////////////
		if (pistol && !swimming && !Boat.onBoat) {
			if (playerDir == 1 && armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerUp1, false, false);
				} else if (walking | running) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerUp2, false, false);
				} else {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerStillUp, false, false);
				}
			} else if (playerDir == 3 && armed) {

				if (walking | running && anim % 20 > 10) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerDown1, false, false);
				} else if (walking | running) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerDown2, false, false);
				} else {
					render.renderPlayer(xx - 16, yy - 16, Sprite.playerStillDown, false, false);

				}
			} else if (playerDir == 0 && armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight2, true, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight1, true, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
				}
			} else if (playerDir == 2 && armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight2, false, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight1, false, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
				}
			}
			///////////////////////////////////////
			if (assaultRifle && !swimming && !Boat.onBoat) {
				if (playerDir == 1 && armed) {
					if (walking | running && anim % 20 > 10) {
						render.render(xx - 16, yy - 16, Sprite.AR_playerUp1, false, false);
					} else if (walking | running) {
						render.render(xx - 16, yy - 16, Sprite.AR_playerUp2, false, false);
					} else {
						render.render(xx - 16, yy - 16, Sprite.AR_playerStillUp, false, false);
					}
				}
				if (playerDir == 3 && armed) {
					if (walking | running && anim % 20 > 10) {
						render.render(xx - 16, yy - 16, Sprite.AR_playerDown1, false, false);
					} else if (walking | running) {
						render.render(xx - 16, yy - 16, Sprite.AR_playerDown2, false, false);
					} else {
						render.render(xx - 16, yy - 16, Sprite.AR_playerStillDown, false, false);

					}
				}
				if (playerDir == 2 && armed) {
					if (walking | running && anim % 20 > 10) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight2, false, false);
					} else if (walking | running && anim % 20 > 3) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, false, false);
					} else if (walking | running) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight1, false, false);
					} else {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, false, false);
					}
				}
				if (playerDir == 0 && armed) {
					if (walking | running && anim % 20 > 10) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight2, true, false);
					} else if (walking | running && anim % 20 > 3) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, true, false);
					} else if (walking | running) {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight1, true, false);
					} else {
						render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, true, false);
					}
				}
			}
		}
		//////////////////////////////////////////////////
		if (!armed && !swimming && !Boat.onBoat) {
			if (playerDir == 1 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.disarmed_playerUp1, false, false);
				} else if (walking | running) {
					render.renderPlayer(xx - 16, yy - 16, Sprite.disarmed_playerUp2, false, false);
				} else {
					render.renderPlayer(xx - 16, yy - 16, Sprite.disarmed_playerStillUp, false, false);
				}
			}
			if (playerDir == 0 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerRight2, true, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerStillRight, true, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerRight1, true, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerStillRight, true, false);
				}
			}
			if (playerDir == 2 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerRight2, false, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerStillRight, false, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerRight1, false, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y - 16, Sprite.disarmed_playerStillRight, false, false);
				}
			}
			if (playerDir == 3 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown1, false, false);
				} else if (walking | running) {
					render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown2, false, false);
				} else {
					render.render(xx - 16, yy - 16, Sprite.disarmed_playerStillDown, false, false);
				}
			}
		}

		if (swimming && !Boat.onBoat) {
			int yz = 0;
			if (!Boat.onBoat) {
				if (time % 45 > 23) {
					yz = -1;
				} else if (time % 23 > 0) {
					yz = 0;
				}
				if (time % 40 > 26) {
					render.renderIcon((int) x - 16, (int) y + 8 + yz, Sprite.splashA, false, false, true);
				} else if (time % 40 > 13) {
					render.renderIcon((int) x - 16, (int) y + 8 + yz, Sprite.splashB, false, false, true);
				} else if (time % 40 > 0) {
					render.renderIcon((int) x - 16, (int) y + 8 + yz, Sprite.splashC, false, false, true);
				}
			}
			if (playerDir == 1 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerUp1_Swimming, false, false);
				} else if (walking | running) {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerUp2_Swimming, false, false);
				} else {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerStillUp_Swimming, false, false);
				}
			}
			if (playerDir == 3 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerDown1_Swimming, false, false);
				} else if (walking | running) {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerDown2_Swimming, false, false);
				} else {
					render.renderPlayer(xx - 16, yy + yz, Sprite.playerStillDown_Swimming, false, false);
				}
			}
			if (playerDir == 2 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerRight2_Swimming, false, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerStillRight_Swimming, false, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerRight1_Swimming, false, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerStillRight_Swimming, false, false);
				}
			}
			if (playerDir == 0 && !armed) {
				if (walking | running && anim % 20 > 10) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerRight2_Swimming, true, false);
				} else if (walking | running && anim % 20 > 3) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerStillRight_Swimming, true, false);
				} else if (walking | running) {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerRight1_Swimming, true, false);
				} else {
					render.renderPlayer((int) x - 16, (int) y + yz, Sprite.playerStillRight_Swimming, true, false);
				}
			}
		}

		///////////////////////////////////////////
		// if (cCentre.activated) cCentre.renderGui(render);
	}

}
