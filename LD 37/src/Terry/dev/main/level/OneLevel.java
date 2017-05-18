package Terry.dev.main.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Terry.dev.main.input.Input;

public class OneLevel extends Level {

	public OneLevel(String path) {
		super(path);
	}

	public void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(OneLevel.class.getResource(path));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int[w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		} catch (IOException e) {
			System.out.println("Could not load level file!");
		}
	}
	protected void ttpLevel() {
	}

}
