package Terry.dev.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;

import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.SpriteSheet;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.OneLevel;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 300, HEIGHT = 250;
	private static final int SCALE = 3;
	private JFrame frame;
	private Thread thread;
	private Render render;
	private Player player;
	private Zombie zombie;
	public static boolean firstSpawn = true;
	private Level level;
	public static boolean finalLevel = false;
	public static boolean infiniLevel = false;
	private Input input;
	public static boolean menu = false;
	private boolean running = false;
	private boolean canChangeLevel = false;
	private String title = "LD37 - Death Room";

	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public void getLevel(Level level) {
		this.level = level;
	}

	public Game() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		render = new Render(WIDTH, HEIGHT, pixels, SpriteSheet.sheet);
		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		level = new OneLevel("/levels/level1.png");
		player = new Player(input, level);
		level.add(player);
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		frame = new JFrame();
		frame.setTitle(title);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		requestFocus();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0;
		final double ns = 1000000000.0 / 60.0;
		int frames = 0;
		int ups = 0;
		init();
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta > 1) {
				ups++;
				tick();
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				// System.out.println(ups + " ticks, " + frames + " FPS");
				frame.setTitle(title + " | " + " ticks, " + frames + " FPS");
				frames = 0;
				ups = 0;
			}
		}
		stop();
	}

	Random random = new Random();
	public int time = 0;

	public static int getWWidth() {
		return WIDTH * SCALE;
	}

	public static int getWHeight() {
		return HEIGHT * SCALE;
	}

	public static void playSound(final String url, Float vol) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.class.getResource(url));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(vol - 20);
			clip.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private int expo = 0;

	public void tick() {
		time++;
		input.tick();
		level.tick();
		if (input.space) {
			menu = false;
		}

		levelTick();
	}

	private BufferedImage main;

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		// render.renderRect(10, 10, 20, 20, 0x57A1BA);
		render.clear();
		int xScroll = (int) (player.getX() - render.width / 2);
		int yScroll = (int) (player.getY() - render.height / 2);

		// if (xScroll < 0) xScroll = 0;
		// if (yScroll < 0) yScroll = 0;
		// if (xScroll >= level.width) xScroll = level.width;
		// if (yScroll >= level.height + (render.height / 2) - 37) yScroll =
		// level.height + (render.height / 2) - 37;

		level.render(xScroll, yScroll, render);
		if (player.dead) {
			String deathMsg = "YOU ARE DEAD";
			Font.draw(deathMsg, render, WIDTH / 2 - deathMsg.length() * 4, HEIGHT / 2 - 8, 0x5E2727, false);
			Font.draw(deathMsg, render, WIDTH / 2 - deathMsg.length() * 4 - 1, HEIGHT / 2 - 8 - 1, 0xC23434, false);

			String Score = "Your Score:" + Integer.toString(Player.score);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 + 1 - 15, (HEIGHT / 2) + 4, 0x614B4B, false);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15, (HEIGHT / 2 - 1) + 4, 0xC99797, false);
		} else {
			String Score = "Your Score:";
			String ScoreNum = Integer.toString(Player.score);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15 + 1, (3), 0x363636, false);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15, (2), 0xEFF589, false);
			Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 1 + 90 - 15, (3), 0x363636, false);
			Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 90 - 15, (2), 0xEF358C, false);
		}

		Graphics g = bs.getDrawGraphics();
		if (menu) {

			try {
				main = ImageIO.read(BufferedImage.class.getResource("/sheets/Main.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		if (menu) {
			String msg = "Can anyone hear me?  i need Help! im in the basement!";
			String msg1 = "Kill all of these zombies to clear the path and please try rescue me!";
			Font.draw(msg, render, render.width / 2, render.height / 2, 0xA16468, false);
			g.drawImage(main, 0, 0, main.getWidth(), main.getHeight(), null);

		}

		g.dispose();
		bs.show();
	}

	int lvl = 0;

	private void levelTick() {
		if (!menu) {
			if (time % (60 * 7) == 0) {
				expo++;
				if (finalLevel) {
					expo += 2;
					for (int i = 0; i < expo - (expo / 3); i++) {
						zombie = new Zombie(level);
						level.add(zombie);
					}
				}
				for (int i = 0; i < expo - (expo / 3); i++) {
					zombie = new Zombie(level);
					level.add(zombie);
				}
			}
		}
		if (expo > 5) expo = 0;
		if (canChangeLevel) {

			if (Player.score == 200 && lvl == 0) {
				lvl = 1;
				expo = 0;
				level.clearLevel();
				System.out.println(0);
				getLevel(new OneLevel("/levels/level2.png"));
				player = new Player(input, level);
				level.add(player);

			}

			if (Player.score == 400 && lvl == 1) {
				lvl = 2;
				level.clearLevel();
				getLevel(new OneLevel("/levels/level3.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 600 && lvl == 2) {
				lvl = 3;
				level.clearLevel();
				getLevel(new OneLevel("/levels/level4.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 800 && lvl == 3) {
				lvl = 4;
				level.clearLevel();
				getLevel(new OneLevel("/levels/level5.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 1000 && lvl == 4) {
				lvl = 5;
				level.clearLevel();
				getLevel(new OneLevel("/levels/levelFinal.png"));
				finalLevel = true;

				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 1100 && lvl == 5) {
				lvl = 6;
				level.clearLevel();
				getLevel(new OneLevel("/levels/level5.png"));
				infiniLevel = true;
				player = new Player(input, level);
				level.add(player);
			}

		}
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
