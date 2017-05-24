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
	private Sprite sprite;
	private boolean inputInitiated = false;

	public Boat(Level level) {
		health = random.nextInt(40) + 20;
		findZombieStartPos(level);
		START_SPEED = Math.abs(random.nextDouble() + 0.1);
		sprite = Sprite.boat;
	}

	public Boat(int x, int y, Level level) {
		this.x = x;
		this.y = y;
		health = random.nextInt(100) + 50;
		START_SPEED = Math.abs(random.nextDouble() - 0.1);
		sprite = Sprite.boat;
	}

	public double playerX;
	public double playerY;
	boolean canEnter = true;

	public void tick() {
		xa = 0;
		ya = 0;
		speed = 2;
		if (level.levelSwitching) this.remove();
		time++;
		anim++;
		List<Player> players = level.getPlayersOffseted(x + 50, y - 20, 60);
		int yz = 2;

		if (players.size() > 0 && level.getTile((int) x / 16, (int) (y / 16) + yz) == Tile.water&& canEnter) {
			Player player = players.get(0);
			this.input = player.input;
			inputInitiated = true;
			if (input.up.down) {
				ya -= speed;
				yz = 5;
				dir = 1;
				player.y = y;
			} else if (input.down.down) {
				ya += speed;
				dir = 3;
				player.y = y;
				yz = 2;
			}
			if (input.left.down) {
				xa -= speed;
				dir = 0;
				player.x = x;
				yz = 5;

			} else if (input.right.down) {
				xa += speed;
				dir = 2;
				player.x = x;
				yz = 2;
			}
			onBoat = true;

			// ya += 0.1;
			// if ((int) x < (int) playerX) xa += speed * 1.2;
			// if ((int) y < (int) playerY) ya += speed * 1.2;
			// if ((int) x > (int) playerX) xa -= speed * 1.2;
			// if ((int) y > (int) playerY) ya -= speed * 1.2;

			speed = START_SPEED;
			if (input.use.clicked && onBoat && inputInitiated) {
				System.out.println(onBoat );
				onBoat = false;
				canEnter = false;
			} 
		} else if(players.size()< 1){
			canEnter = true;
			onBoat = false;
		}
		

		if (xa != 0 || ya != 0) {
			walking = true;
			if (!debug) {
				move2(xa, 0);
				move2(0, ya);
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
		int xx = (int) x - sprite.width / 2;
		int yy = (int) y - (sprite.height / 5);

		if (dir == 0) { // left
			render.renderWH((int) x - Sprite.boat.width / 2, (int) y - (Sprite.boat.height / 5), Sprite.boat, false, false, false);
			if (onBoat) render.renderPlayer(xx + (Sprite.boatN.width / 2) + 30, yy + 10, Sprite.playerStillRight_Swimming, true, false);
		} else if (dir == 1) { // up
			render.renderWH((int) x - Sprite.boatN.width / 2, (int) y - Sprite.boatN.height / 3, Sprite.boatN, false, false, false);
			if (onBoat) render.renderPlayer((int) x - Sprite.boatN.width / 2 + 15, yy + (Sprite.boatN.height / 4) - 24, Sprite.playerStillUp_Swimming, false, false);
		} else if (dir == 2) { // right
			render.renderWH((int) x - Sprite.boat.width / 2, (int) y - (Sprite.boat.height / 5), Sprite.boat, true, false, false);
			if (onBoat) render.renderPlayer(xx + 62, yy + 10, Sprite.playerDown1_Swimming, false, false);
		} else if (dir == 3) { // down
			render.renderWH((int) x - Sprite.boatN.width / 2, (int) y - Sprite.boatN.height / 3, Sprite.boatS, false, false, false);
			if (onBoat) render.renderPlayer((int) x - Sprite.boatN.width / 2 + 15, yy + (Sprite.boatS.height / 4) - 30, Sprite.playerStillDown_Swimming, false, false);
		}
		// if (time % 60 > 40) {
		// render.renderWH((int) x, (int) y, Sprite.boat, false, false, false);
		// } else if (time % 60 > 20) {
		// render.renderWH((int) x, (int) y, Sprite.boat1, false, false, false);
		// } else {
		// render.renderWH((int) x, (int) y, Sprite.boat2, false, false, false);
		// }

		/*
		 * if (dir == 1) { if (walking && anim % 20 > 10) { render.renderMob(xx
		 * - 16, yy - 16, Sprite.zombieUp1, false, false, col); } else if
		 * (walking) { render.renderMob(xx - 16, yy - 16, Sprite.zombieUp2,
		 * false, false, col); } else { render.renderMob(xx - 16, yy - 16,
		 * Sprite.zombieStillUp, false, false, col); } }
		 * 
		 * if (dir == 3) { if (walking && anim % 20 > 10) { render.renderMob(xx
		 * - 16, yy - 16, Sprite.zombieDown1, false, false, col); } else if
		 * (walking) { render.renderMob(xx - 16, yy - 16, Sprite.zombieDown2,
		 * false, false, col); } else { render.renderMob(xx - 16, yy - 16,
		 * Sprite.zombieStillDown, false, false, col);
		 * 
		 * } }
		 * 
		 * if (dir == 2) { if (walking && anim % 20 > 10) {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieRight2,
		 * false, false, col); } else if (walking && anim % 20 > 3) {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieStillRight,
		 * false, false, col); } else if (walking) { render.renderMob((int) x -
		 * 16, (int) y - 16, Sprite.zombieRight1, false, false, col); } else {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieStillRight,
		 * false, false, col);
		 * 
		 * } }
		 * 
		 * if (dir == 0) { if (walking && anim % 20 > 10) {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieRight2,
		 * true, false, col); } else if (walking && anim % 20 > 3) {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieStillRight,
		 * true, false, col); } else if (walking) { render.renderMob((int) x -
		 * 16, (int) y - 16, Sprite.zombieRight1, true, false, col); } else {
		 * render.renderMob((int) x - 16, (int) y - 16, Sprite.zombieStillRight,
		 * true, false, col); } }
		 */

		// Font.draw(Integer.toString(health), render, (xx - 10) + 3, (yy - 28)
		// + 3, 0x694A58, true);
		// Font.draw(Integer.toString(health), render, (xx - 10) + 2, (yy - 28)
		// + 2, 0x9E7286, true);
	}
}
