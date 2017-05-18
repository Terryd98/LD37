package Terry.dev.main.entity.particle;

import Terry.dev.main.entity.Entity;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;

public class Particle extends Entity {

	protected int lifeTime;
	protected Sprite sprite;
	protected double xa, ya, za;
	protected double xx, yy, zz;
	private boolean xFlip, yFlip;
	private int time = 0;
	
	public Particle(int x, int y, int lifeTime, Sprite sprite) {
		this.sprite = sprite;
		this.lifeTime = lifeTime + (random.nextInt(20) - 10);
		this.x = x;
		this.y = y;
		this.xx = x;
		this.yy = y;
		xFlip = random.nextBoolean();
		yFlip = random.nextBoolean();

		this.xa = random.nextGaussian();
		this.ya = random.nextGaussian();
		this.zz = random.nextGaussian() + 8.0;
	}

	public static boolean lremove = false;

	public void tick() {
		time++;
		if (time >= (Integer.MAX_VALUE - 10)) time = 0;

		if (lremove || time >= lifeTime) {
			level.remove(this);
			lremove = false;
		}
		za -= 0.1;

		if (zz < 0) {
			zz = 0;
			za *= -0.55;
			xa *= 0.2;
			ya *= 0.2;
		}

		tickPos(xx + xa, (yy + ya) + zz + za);
	}

	public void tickPos(double x, double y) {
		if (Collision(x, y)) {
			this.xa *= -0.5;
			this.ya *= -0.5;
			this.za *= -0.5;
		}
		this.xx += xa;
		this.yy += ya;
		this.zz += za;
	}

	public boolean Collision(double x, double y) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x - c % 2 * Sprite.T_SIZE + 1.5) / Sprite.T_SIZE);
			double yt = ((y - c / 2 * Sprite.T_SIZE) / Sprite.T_SIZE);
			if (x < 0) x = 0;
			if (y < 0) y = 0;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			if (level.getTile(ix, iy).Entitysolid()) {
				return solid = true;
			}
		}
		return solid;
	}

	public void render(Render render) {
		render.renderParticle((int) xx + 1, (int) (yy - (int) zz) + 1, sprite, xFlip, yFlip);
	}

}
