package Terry.dev.main.gfx;

public class Render {

	public int width, height;
	public int[] pixels;
	private int xOffset, yOffset;
	private SpriteSheet sheet;

	public Render(int width, int height, int[] pixels, SpriteSheet sheet) {
		this.sheet = sheet;
		this.width = width;
		this.height = height;
		this.pixels = pixels;

	}

	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public void render(int xp, int yp, Sprite sprite, boolean xFlip, boolean yFlip) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.SIZE; y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = (sprite.SIZE - 1) - y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xs = x;
				if (xFlip) xs = (sprite.SIZE - 1) - x;
				if (xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs + ys * sprite.SIZE];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderIcon(int xp, int yp, Sprite sprite, boolean xFlip, boolean yFlip, boolean fixed) {
		if (fixed) {
			xp -= xOffset;
			yp -= yOffset;

		}
		for (int y = 0; y < sprite.SIZE; y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = (sprite.SIZE - 1) - y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xs = x;
				if (xFlip) xs = (sprite.SIZE - 1) - x;
				if (xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs + ys * sprite.SIZE];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderParticle(int xp, int yp, Sprite sprite, boolean xFlip, boolean yFlip) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = (sprite.getHeight() - 1) - y;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xp;
				int xs = x;
				if (xFlip) xs = (sprite.getWidth() - 1) - x;
				if (xa < -sprite.getWidth() || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs + ys * sprite.getWidth()];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void render(int xp, int yp, int tile, int colors, boolean xFlip, boolean yFlip, boolean following) {
		if (following) {
			xp -= xOffset;
			yp -= yOffset;
		}

		int xTOff = tile % 32;
		int yTOff = tile / 32;
		int tileOffs = xTOff * 8 + yTOff * 8 * sheet.SIZE;

		for (int y = 0; y < 8; y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = 7 - y;
			for (int x = 0; x < 8; x++) {
				int xa = x + xp;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) break;
				int xs = x;
				if (xFlip) xs = 7 - x;
				int col = ((sheet.pixels[xs + ys * sheet.SIZE + tileOffs]));
				if (col != 0xffff00ff) {
					if (col == 0xffffffff) col = colors;
					pixels[xa + ya * width] = col;

				}
			}
		}
	}

	public void renderRect(int x0, int y0, int xSize, int ySize, int col, boolean following) {
		if (following) {
			x0 -= xOffset;
			y0 -= yOffset;
		}
		for (int yt = y0; yt < y0 + ySize; yt++) {
			int ya = yt + y0;
			for (int xt = x0; xt < x0 + xSize; xt++) {
				int xa = xt + x0;
				if (xa < -16 || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderPlayer(int xp, int yp, Sprite sprite, boolean xFlip, boolean yFlip) {
		xp -= xOffset;
		yp -= yOffset;
		for (int y = 0; y < sprite.SIZE; y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = (sprite.SIZE - 1) - y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xs = x;
				if (xFlip) xs = (sprite.SIZE - 1) - x;
				if (xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs + ys * sprite.SIZE];
				if (col != 0xffff00ff) pixels[xa + ya * width] = col;
			}
		}
	}

	public void renderZombie(int xp, int yp, Sprite sprite, boolean xFlip, boolean yFlip, int cCol) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < sprite.SIZE; y++) {
			int ya = y + yp;
			int ys = y;
			if (yFlip) ys = (sprite.SIZE - 1) - y;
			for (int x = 0; x < sprite.SIZE; x++) {
				int xa = x + xp;
				int xs = x;
				if (xFlip) xs = (sprite.SIZE - 1) - x;
				if (xa < -sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
				if (xa < 0) xa = 0;
				int col = sprite.pixels[xs + ys * sprite.SIZE];
				if (col == 0xff416144) col = cCol;
				if (col != 0xffff00ff) {
					if (cCol == 0xffffffff) {
						col = 0xffffffff;
					}
					pixels[xa + ya * width] = col;
				}
			}
		}
	}

	public void setOffsets(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
