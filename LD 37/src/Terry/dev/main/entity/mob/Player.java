package Terry.dev.main.entity.mob;

import Terry.dev.main.Game;
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
	public boolean dead = false;
	public double speed = WALKING_SPEED;
	private static int energy = 100;
	public static int cash = 0;
	public static int addedCash = 0;
	public static int addedAmmo = 0;

	public int tickCount = 0;
	private static boolean armed = true;
	private static final double WALKING_SPEED = 1;
	private static final double RUNNING_SPEED = 1.5;
	public static int score = 0;
	private boolean carrying = false;
	private int time = 0;
	private Trap trap;
	private CommandCentre cCentre;
	private GrenadeEntity grenade;
	private int placeDir = 0;
	public static int traps = 0;
	private final int SHAKE_TIME = 10;
	private int shakeTime = SHAKE_TIME;
	Projectile p;
	public int cashPickupTime = 10;
	public int ammoPickupTime = 10;
	public static boolean pistol = true;
	public static boolean shotgun = false;
	public static boolean assaultRifle = false;

	public static boolean hasPistol = true;
	public static boolean hasShotgun = false;
	public static boolean hasAssaultRifle = false;

	private static int PISTOL_CLIP = PistolBullet.CLIP;
	public static int PISTOL_AMMO = PistolBullet.AMMO;
	private int pistol_fireRate = PistolBullet.FIRERATE;

	private static int ASSAULT_RIFLE_CLIP = AssaultRifle.CLIP;
	public static int ASSAULT_RIFLE_AMMO = AssaultRifle.AMMO;
	private int ASSAULT_RIFLE_fireRate = AssaultRifle.FIRERATE;

	public Player(Input input, Level level) {
		this.input = input;
		findStartPos(level);
		cCentre = new CommandCentre((int) x + 8, (int) y, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x, y, level, input);
		level.add(drawer);
	}

	public Player(int x, int y, Input input, Level level) {
		this.input = input;
		this.x = x;
		this.y = y;
		cCentre = new CommandCentre((int) x + 8, (int) y, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x, y, level, input);
		level.add(drawer);
	}

	public Player(Vector2i vector, Input input, Level level) {
		this.input = input;
		this.x = vector.x;
		this.y = vector.y;
		vector.x += 8;
		cCentre = new CommandCentre(vector, level);
		level.add(cCentre);
		DrawerEntity drawer = new DrawerEntity(x - 2 * 16, y - 2 * 16, level, input);
		level.add(drawer);
	}

	public void tick() {
		if (addedCash > 0 || addedAmmo > 0) {
			if (cashPickupTime == 0) {
				tickCount = 0;
				cashPickupTime = 50;
			}
			if (cashPickupTime > 0) cashPickupTime--;
			if (ammoPickupTime == 0) {
				tickCount = 0;
				ammoPickupTime = 50;
			}
			if (ammoPickupTime > 0) ammoPickupTime--;

			if (anim % 10 == 1) tickCount++;
		}
		if (DrawerEntity.inRange && input.use.clicked && !trapToggled) {
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
			cCentre.x = x - 8;
			cCentre.y = y - 20;
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
		if (trapToggled || carrying || cCentre.activated) {
			armed = false;
		} else {
			armed = true;
		}
		if (input.trap.clicked && !cCentre.activated && !DrawerEntity.looting) {
			trapToggled = true;

		} else {
			trapToggled = false;
		}
		if (input.space.clicked && trapToggled && !carrying) {
			place();
		}

		if (!walking && !running) {
			still = true;
		} else still = false;
		anim++;

		time++;
		if (energy <= 0) energy = 0;
		if (energy >= 100) energy = 100;

		if (energy < 100 && ((!running && walking) || (!running && !walking)) && !carrying && anim % 15 == 0) {
			if (still) energy += 5;

			energy++;
		}

		if (!carrying && input.shift.down && energy > 0) {
			speed = RUNNING_SPEED;
			if (running && anim % 5 == 0) energy--;
		} else {
			speed = WALKING_SPEED;
		}

		double xa = 0, ya = 0;
		if (!cCentre.activated && !DrawerEntity.looting) {
			if (carrying) {
				running = false;
				walking = true;
				speed = 0.7;
			}
			if (input.up.down) {
				ya -= speed;
			} else if (input.down.down) {
				ya += speed;
			}
			if (input.left.down) {
				xa -= speed;
			} else if (input.right.down) {
				xa += speed;
			}
		}

		// TODO: Temp
		if (input.mouseB == 3 && pistol_fireRate == 0) {
			grenade = new GrenadeEntity(x, y, level);
			level.add(grenade);
			pistol_fireRate = PistolBullet.FIRERATE;

		}
		if (xa != 0 || ya != 0) {
			if (speed == WALKING_SPEED) {
				walking = true;
				running = false;
			}
			if (speed == RUNNING_SPEED) {
				running = true;
				walking = false;
			}
			move(xa, 0);
			move(0, ya);
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
			/*if (running) {
				Game.shake = 2;
			} else */if (assaultRifle && shooting) {
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

	public boolean reload = false;
	public boolean shooting = false;
	public int reloadTime = 100;
	public final int RELOAD_TIME = 100;

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
					if (dir == 0) {
						shoot(x - 7, y + 4, direction, 1);
						PISTOL_CLIP--;
					}
					if (dir == 1) {
						shoot(x - 7, y + 4, direction, 1);
						PISTOL_CLIP--;

					}
					if (dir == 2) {
						shoot(x - 6, y + 4, direction, 1);
						PISTOL_CLIP--;

					}
					if (dir == 3) {
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
					if (dir == 0) {
						shoot(x - 7, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;
					}
					if (dir == 1) {
						shoot(x - 7, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;

					}
					if (dir == 2) {
						shoot(x - 6, y + 4, direction, 3);
						ASSAULT_RIFLE_CLIP--;

					}
					if (dir == 3) {
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

	private int finalMessageTime = 0;
	boolean trapToggled = false;

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

		Font.draw("Energy:", render, (render.width - 82), render.height - 10, 0x363636, false);
		Font.draw("Energy:", render, (render.width - 82), render.height - 11, 0xEFF589, false);

		Font.draw(Integer.toString(energy), render, (render.width - 26), render.height - 10, 0x7E305C, false);
		Font.draw(Integer.toString(energy), render, (render.width - 26), render.height - 11, 0xEF358C, false);

		Font.draw("Cash:", render, (render.width - 82), render.height - 20, 0x363636, false);
		Font.draw("Cash:", render, (render.width - 82), render.height - 21, 0xEFF589, false);

		Font.draw(Integer.toString(cash), render, (render.width - 42), render.height - 20, 0x7E305C, false);
		Font.draw(Integer.toString(cash), render, (render.width - 42), render.height - 21, 0xEF358C, false);

		if (addedCash > 0) {
			if (cashPickupTime > 0) {
				Font.draw("+" + Integer.toString(addedCash), render, (render.width - 115), render.height - (tickCount) - 20, 0x7E305C, false);
				Font.draw("+" + Integer.toString(addedCash), render, (render.width - 115), render.height - (tickCount) - 21, 0xEF358C, false);
			} else {
				cashPickupTime = 0;
				addedCash = 0;
			}
		}

		if (addedAmmo > 0) {
			if (ammoPickupTime > 0) {
				Font.draw("+" + Integer.toString(addedAmmo), render, (render.width - 50), render.height - (tickCount) - 240, 0x7E305C, false);
				Font.draw("+" + Integer.toString(addedAmmo), render, (render.width - 50), render.height - (tickCount) - 241, 0xEF358C, false);
			} else {
				ammoPickupTime = 0;
				addedAmmo = 0;
			}
		}

		if (dir == 1) {
			render.render(xx - 8, yy + 6, Sprite.shadow, false, false);
		}
		if (dir == 3 ) {
			render.render(xx - 8, yy + 6, Sprite.shadow, false, true);
		}
		if (dir == 0) {
			render.render(xx - 11 , yy + 7, Sprite.shadow, false, false);
		}
		if (dir == 2 ) {
			render.render(xx -5, yy + 7, Sprite.shadow, false, false);
		}
		
		render.renderRect(0, 0, Game.getWWidth(), 13, 0x303030, false);
		render.renderRect(0, 0, Game.getWWidth(), 12, 0x848484, false);

		if (pistol) {/////////////////////////////////////////
			if (PISTOL_CLIP > 0) {

				Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
				Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
			}

			if (PISTOL_CLIP <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
					Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 3, 0x5C5E38, false);
					Font.draw(Integer.toString(PISTOL_CLIP), render, (render.width - 17), 2, 0xB1B564, false);
				}
			}

			if (PISTOL_AMMO <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
					Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x5C5E38, false);
					Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xB1B564, false);
				}
			}
			if (PISTOL_AMMO > 0) {
				Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
				Font.draw(Integer.toString(PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
			}
		}
		if (assaultRifle) {//////////////////////////////////////////////
			if (ASSAULT_RIFLE_CLIP > 0) {

				Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
				Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
			}

			if (ASSAULT_RIFLE_CLIP <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
					Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x5C5E38, false);
					Font.draw(Integer.toString(ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xB1B564, false);
				}
			}

			if (ASSAULT_RIFLE_AMMO <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
					Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x5C5E38, false);
					Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xB1B564, false);
				}
			}
			if (ASSAULT_RIFLE_AMMO > 0) {
				Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
				Font.draw(Integer.toString(ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
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

		Font.draw("Health:", render, 3, 3, 0x363636, false);
		Font.draw("Health:", render, 2, 2, 0xEFF589, false);

		Font.draw(Integer.toString(health), render, 3 + 57, 3, 0x363636, false);
		Font.draw(Integer.toString(health), render, 2 + 57, 2, 0xEF358C, false);

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
		{
			{
				if (pistol) {
					if (dir == 1 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.playerUp1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.playerUp2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.playerStillUp, false, false);
						}
					} else if (dir == 1 && !armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerUp1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerUp2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerStillUp, false, false);
						}
					}
					if (dir == 3 && armed) {

						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.playerDown1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.playerDown2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.playerStillDown, false, false);

						}
					} else if (dir == 3 && !armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerStillDown, false, false);
						}
					}
					if (dir == 2 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight2, false, false);
						} else if (walking | running && anim % 20 > 3) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
						} else if (walking | running) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight1, false, false);
						} else {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, false, false);
						}
					} else if (dir == 2 && !armed) {
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
					if (dir == 0 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight2, true, false);
						} else if (walking | running && anim % 20 > 3) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
						} else if (walking | running) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerRight1, true, false);
						} else {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.playerStillRight, true, false);
						}
					} else if (dir == 0 && !armed) {
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
				}

				if (assaultRifle) {
					if (dir == 1 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.AR_playerUp1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.AR_playerUp2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.AR_playerStillUp, false, false);
						}
					} else if (dir == 1 && !armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerUp1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerUp2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerStillUp, false, false);
						}
					}
					if (dir == 3 && armed) {

						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.AR_playerDown1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.AR_playerDown2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.AR_playerStillDown, false, false);

						}
					} else if (dir == 3 && !armed) {
						if (walking | running && anim % 20 > 10) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown1, false, false);
						} else if (walking | running) {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerDown2, false, false);
						} else {
							render.render(xx - 16, yy - 16, Sprite.disarmed_playerStillDown, false, false);
						}
					}
					if (dir == 2 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight2, false, false);
						} else if (walking | running && anim % 20 > 3) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, false, false);
						} else if (walking | running) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight1, false, false);
						} else {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, false, false);
						}
					} else if (dir == 2 && !armed) {
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
					if (dir == 0 && armed) {
						if (walking | running && anim % 20 > 10) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight2, true, false);
						} else if (walking | running && anim % 20 > 3) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, true, false);
						} else if (walking | running) {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerRight1, true, false);
						} else {
							render.renderPlayer((int) x - 16, (int) y - 16, Sprite.AR_playerStillRight, true, false);
						}
					} else if (dir == 0 && !armed) {
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
				}
			}
		}
		if (hasPistol) {
			render.renderIcon(render.width - 19, render.height - 40, Sprite.pistolIconOff, false, false, false);
			if (pistol) {
				render.renderIcon(render.width - 19, render.height - 40, Sprite.pistolIconOn, false, false, false);
			}

		}
		if (hasAssaultRifle) {
			render.renderIcon(render.width - 19, render.height - 60, Sprite.assaultRifleIconOff, false, false, false);
			if (assaultRifle) {
				render.renderIcon(render.width - 19, render.height - 60, Sprite.assaultRifleIconOn, false, false, false);
			}
		}

		///////////////////////////////////////////
		// if (cCentre.activated) cCentre.renderGui(render);
	}

}
