package Terry.dev.main.entity;

import java.util.List;

import Terry.dev.main.Game;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.level.Level;

public class Trap extends Entity {

	private Sprite sprite;
	public static boolean solid = false;
	private int tickCount = 0;
	private int time = 15;
	private int health = 100;

	public Trap(int x, int y, Level level) {
		this.x = x;
		this.y = y;
		sprite = Sprite.spikes;
		Game.playSound("/sounds/hit.wav", -5.0f);
	}

	public void tick() {
		if (health <= 0) {
			Game.playSound("/sounds/break.wav", -5.0f);
			level.remove(this);
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
				zombie.hurt(5);
				time = 15;
			}
		}
		
		if (chaser.size() > 0) {
			ChasingZombie chasers = chaser.get(0);
			chasers.speed = chasers.START_SPEED / 2;
			if (time >= 0) time--;
			if (time == 0) {
				sprite = Sprite.spikesBlood;
				health -= 5;
				chasers.hurt(5);
				time = 15;
			}
		}
	}

	public void render(Render render) {
		render.render((int) x * 16, (int) y * 16, sprite, false, false);
	}

}
