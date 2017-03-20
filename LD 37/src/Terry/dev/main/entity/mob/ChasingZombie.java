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
import Terry.dev.main.level.Node;
import Terry.dev.main.level.Tile;
import Terry.dev.main.util.Vector2i;

public class ChasingZombie extends Mob {

	public double speed;
	public final double START_SPEED;
	private int time = 0;
	double xa = 0, ya = 0;
	public int health;
	private int cCol;
	private int col;
	private List<Node> path = null;
	public int damage = 1;
	private int counter =0;
	public boolean debug = false;

	public ChasingZombie(Level level) {
		findZombieStartPos(level);
		START_SPEED = 1;// Math.abs(random.nextDouble() - 0.1);
		health = random.nextInt(75);
		cCol = random.nextInt(4);
		if (cCol == 0) col = 0x4D8254;
		if (cCol == 1) col = 0x0E3012;
		if (cCol == 2) col = 0x4D8254;
		if (cCol == 3) col = 0x0E3012;

	}

	private boolean playerInRange = false;

	public ChasingZombie(int x, int y, Level level) {
		this.x = x;
		this.y = y;
		health = random.nextInt(100) - 70;
		START_SPEED = 1;// Math.abs(random.nextDouble() - 0.1);
		cCol = random.nextInt(4);
		if (cCol == 0) col = 0x0E3012;
		if (cCol == 1) col = 0x4D8254;
		if (cCol == 2) col = 0x0E3012;
		if (cCol == 3) col = 0x4D8254;

	}

	public void tick() {
		if (level.getTile((int) (x) / 16, (int) (y + 16) / 16) == Tile.flower) {
			level.setTile((int) x / 16, (int) ((y + 16) / 16), Tile.grass);
			if (playerInRange) Game.playSound("/sounds/flower.wav", -10.0f);
		}
		List<Player> players = level.getPlayers(this, 150);
		time++;
		anim++;
		if (!debug) {
			damage = 1;
			if (players.size() <= 0) playerInRange = false;
			if (players.size() > 0) {
				counter++;
				if (counter > 70) {
					playerInRange = true;
					xa = 0;
					ya = 0;
					int px = (int) level.getPlayerAt(0).x;
					int py = (int) level.getPlayerAt(0).y;
					Vector2i start = new Vector2i((int) x >> 4, (int) y >> 4);
					Vector2i dest = new Vector2i(px >> 4, py >> 4);
					path = level.findPath(start, dest);
					if (path != null) {
						if (path.size() > 0) {
							Vector2i vec = path.get(path.size() - 1).tile;
							if (x < vec.x * 16) xa += speed;
							if (x > vec.x * 16) xa -= speed;
							if (y < vec.y * 16) ya += speed;
							if (y > vec.y * 16) ya -= speed;
						}
					}
					speed = START_SPEED;

					if (time % (random.nextInt(2000) + 540) == 0) {
						Game.playSound("/sounds/zombie.wav", -8.0f);
					}
					if (time % (random.nextInt(2000) + 540) == 0) {
						Game.playSound("/sounds/zombie2.wav", -8.0f);
					}
				}
			} /*else {
				counter =0;
				if (time % (random.nextInt(60) + 30) == 0) {
					xa = random.nextInt((int) 2.5) - 0.5;
					ya = random.nextInt((int) 2.5) - 0.5;
					if (random.nextInt(4) == 0) {
						xa = 0;
						ya = 0;
					}
				}
			}*/
		}

		attack();
		if (xa != 0 || ya != 0) {
			walking = true;
			if (!debug) {
				move(xa, 0);
				move(0, ya);
			}
		} else {
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
			} else if (random.nextInt(3) == 1) {
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.rottenArm));
			} else {
				level.add(new ParticleEmitter((int) x, (int) y, 1, 1000, level, Sprite.blood));
			}
			if (random.nextInt(3) < 2) {
				CashEntity cash = new CashEntity(x+random.nextInt(20), y+random.nextInt(20), level);
				level.add(cash);
			}
			
			if (random.nextInt(3) < 1) {
				AmmoEntity ammo = new AmmoEntity(x+random.nextInt(20), y+random.nextInt(20), level);
				level.add(ammo);
			}
			Player.score += 15;
			Game.ZCount--;
			level.remove(this);
		}

	}

}
