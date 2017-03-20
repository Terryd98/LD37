package Terry.dev.main.entity.mob;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.AmmoEntity;
import Terry.dev.main.entity.CashEntity;
import Terry.dev.main.entity.Emitter.ParticleEmitter;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.Tile;

public class Zombie extends Mob {

	public double speed;
	public final double START_SPEED;
	private int time = 0;
	double xa = 0, ya = 0;
	public int health;
	private int cCol;
	public static boolean moving = false;
	private int col;
	private int counter = 0;
	public int damage = 1;
	public boolean debug = false;

	public Zombie(Level level) {
		health = random.nextInt(40) + 20;
		findZombieStartPos(level);
		START_SPEED = Math.abs(random.nextDouble() + 0.1);
		cCol = random.nextInt(4);
		if (cCol == 0) col = 0x76A07B;
		if (cCol == 1) col = 0x94837C;
		if (cCol == 2) col = 0x676975;
		if (cCol == 3) col = 0x60534B;

	}

	public Zombie(int x, int y, Level level) {
		this.x = x;
		this.y = y;
		health = random.nextInt(100) + 50;

		START_SPEED = Math.abs(random.nextDouble() - 0.1);
		cCol = random.nextInt(4);
		if (cCol == 0) col = 0x76A07B;
		if (cCol == 1) col = 0x770039;
		if (cCol == 2) col = 0x64A07B;
		if (cCol == 3) col = 0xC2C2C2;

	}

	public static boolean playerInRange = false;
	double playerX;
	double playerY;

	public void tick() {

		time++;
		anim++;
		if (!debug) {
			damage = 1;
			if (level.getTile((int) (x) / 16, (int) (y + 16) / 16) == Tile.flower) {
				level.setTile((int) x / 16, (int) ((y + 16) / 16), Tile.grass);
				if (playerInRange) Game.playSound("/sounds/flower.wav", -10.0f);
			}
			List<Player> players = level.getPlayers(this, 150);
			if (players.size() <= 0) playerInRange = false;
			if (players.size() > 0) {
				counter++;
				if (counter >= 70) {
					playerInRange = true;
					Player player = players.get(0);
					xa = 0;
					ya = 0;
					if (anim % 30 == 0) {
						playerX = player.getX();
						playerY = player.getY();
					}
					if ((int) x < (int) playerX) xa += speed;
					if ((int) y < (int) playerY) ya += speed;
					if ((int) x > (int) playerX) xa -= speed;
					if ((int) y > (int) playerY) ya -= speed;

					speed = START_SPEED;
					if (time % (random.nextInt(5000) + 540) == 0) {
						Game.playSound("/sounds/zombie.wav", -13.0f);
					}
					if (time % (random.nextInt(5000) + 540) == 0) {
						Game.playSound("/sounds/zombie2.wav", -13.0f);
					}
				} else if (time % (random.nextInt(60) + 30) == 0) {
					counter = 0;
					xa = random.nextInt((int) 2.5) - 0.5;
					ya = random.nextInt((int) 2.5) - 0.5;
					if (random.nextInt(4) == 0) {
						xa = 0;
						ya = 0;
					}
				}
			}
		}

		attack();
		if (xa != 0 || ya != 0) {
			walking = true;
			if (!debug) {
				move(xa, 0);
				move(0, ya);
			}
		} else {
			moving = false;
			walking = false;

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

	public void render(Render render) {
		int xx = (int) x;
		int yy = (int) y;
		render.render(xx - 8, yy + 5, Sprite.shadow, false, false);
		if (dir == 1) {
			if (walking && anim % 20 > 10) {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieUp1, false, false, col);
			} else if (walking) {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieUp2, false, false, col);
			} else {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieStillUp, false, false, col);
			}
		}

		if (dir == 3) {
			if (walking && anim % 20 > 10) {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieDown1, false, false, col);
			} else if (walking) {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieDown2, false, false, col);
			} else {
				render.renderZombie(xx - 16, yy - 16, Sprite.zombieStillDown, false, false, col);

			}
		}

		if (dir == 2) {
			if (walking && anim % 20 > 10) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieRight2, false, false, col);
			} else if (walking && anim % 20 > 3) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieStillRight, false, false, col);
			} else if (walking) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieRight1, false, false, col);
			} else {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieStillRight, false, false, col);

			}
		}

		if (dir == 0) {
			if (walking && anim % 20 > 10) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieRight2, true, false, col);
			} else if (walking && anim % 20 > 3) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieStillRight, true, false, col);
			} else if (walking) {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieRight1, true, false, col);
			} else {
				render.renderZombie((int) x - 16, (int) y - 16, Sprite.zombieStillRight, true, false, col);
			}
		}

		Font.draw(Integer.toString(health), render, (xx - 10) + 3, (yy - 28) + 3, 0x694A58, true);
		Font.draw(Integer.toString(health), render, (xx - 10) + 2, (yy - 28) + 2, 0x9E7286, true);
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

}
