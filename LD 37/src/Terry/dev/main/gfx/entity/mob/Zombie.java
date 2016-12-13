package Terry.dev.main.gfx.entity.mob;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.gfx.entity.Emitter.ParticleEmitter;
import Terry.dev.main.level.Level;

public class Zombie extends Mob {

	public double speed;
	private int time = 0;
	double xa = 0, ya = 0;
	public int health = 70;
	private int cCol;
	private int col;
	public int damage = 1;
	public boolean debug = false;

	public Zombie(Level level) {
		findStartPos(level);
		speed = Math.abs(random.nextDouble() - 0.1);
		cCol = random.nextInt(4);
		if (cCol == 0) col = 0x76A07B;
		if (cCol == 1) col = 0x770039;
		if (cCol == 2) col = 0x64A07B;
		if (cCol == 3) col = 0x77A02B;

	}

	public void tick() {
		time++;
		anim++;
		if (!debug) {
			damage = 1;

			List<Player> players = level.getPlayers(this, 120);
			if (players.size() > 0) {
				Player player = players.get(0);
				xa = 0;
				ya = 0;
				if ((int) x < (int) player.getX()) xa += speed;
				if ((int) y < (int) player.getY()) ya += speed;
				if ((int) x > (int) player.getX()) xa -= speed;
				if ((int) y > (int) player.getY()) ya -= speed;

				if (time % (random.nextInt(2000) + 540) == 0) {
					Game.playSound("/sounds/zombie.wav", -10.0f);
				}
				if (time % (random.nextInt(2000) + 540) == 0) {
					Game.playSound("/sounds/zombie2.wav", -10.0f);
				}
			} else if (time % (random.nextInt(60) + 30) == 0) {
				xa = random.nextInt((int) 2.5) - 0.5;
				ya = random.nextInt((int) 2.5) - 0.5;
				if (random.nextInt(4) == 0) {
					xa = 0;
					ya = 0;
				}
			}
		}

		attack();
		if (xa != 0 || ya != 0) {
			walking = true;

			move(xa, 0);
			move(0, ya);
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
		health -= damage*20;
		Game.playSound("/sounds/hurt.wav", -10.0f);
		level.add(new ParticleEmitter((int) x, (int) y, 50, 2000, level, Sprite.bloodParticle));
		if (health <= 0) {
			if (Player.CLIPS != 100) {
				Player.CLIPS += 5;
			}
			Player.score += 10;
			level.remove(this);
		}

	}

}
