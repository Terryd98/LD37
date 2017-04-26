package Terry.dev.main.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

	public String path;
	public int SIZE;
	public int[] pixels;

	public static SpriteSheet tiles_sheet = new SpriteSheet(256, "/sheets/tiles/spritesheet.png");
	public static SpriteSheet player_sheet = new SpriteSheet(320, "/sheets/player_sheets/player_spritesheet.png");
	public static SpriteSheet zombie_sheet = new SpriteSheet(320, "/sheets/mob_sheets/zombie_spritesheet.png");
	public static SpriteSheet rat_sheet = new SpriteSheet(320, "/sheets/mob_sheets/rat_spritesheet.png");

	public SpriteSheet(int size, String path) {
		this.path = path;
		this.SIZE = size;
		pixels = new int[SIZE * SIZE];
		load();
	}

	private void load() {
		try {
			BufferedImage img = ImageIO.read(BufferedImage.class.getResource(path));
			int w = img.getWidth();
			int h = img.getHeight();
			img.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			System.out.println("PROBLEM LOADING SPRITESHEET");
			e.printStackTrace();
		}
	}

}
