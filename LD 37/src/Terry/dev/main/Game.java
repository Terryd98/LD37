package Terry.dev.main;

import java.awt.Canvas;
import java.awt.Color;
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

import Terry.dev.main.entity.CommandCentre;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.SpriteSheet;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.OneLevel;
import Terry.dev.main.util.Vector2i;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 300, HEIGHT = 267;
	public static int SCALE = 3;
	private JFrame frame;
	private Thread thread;
	private Render render;
	private Player player;
	private Zombie zombie;
	private ChasingZombie chasingZombie;
	public static int shake = 0;
	private int alpha = 0;
	private int tickCount = 0;
	public static boolean firstSpawn = true;
	private Level level;
	public static boolean finalLevel = false;
	public static boolean infiniLevel = false;
	private static Input input;
	public static boolean menu = false;
	public boolean paused = false;
	private boolean running = false;
	private boolean canChangeLevel = false;
	private String title = "LD37 - Death Room";
	private int ZCount = 0;
	private boolean dayNightCycle = false;

	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public void getLevel(Level level) {
		this.level = level;
	}

	public Game() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		render = new Render(WIDTH, HEIGHT, pixels, SpriteSheet.tiles_sheet);
		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		level = new OneLevel("/levels/level1.png");
		//Vector2i pp = new Vector2i(5 * 16, 5 * 16);
		player = new Player(input, level);
		for (int i = 0; i < 0; i++) {
			zombie = new Zombie(level);
			level.add(zombie);
		}
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
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		init();
		while (running) {
			//if (input.esc) {
			//	System.out.println("EXITING..... NO ERROR");
			//	System.exit(0);
			//}
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				if (!paused) tick();
				updates++;
				delta--;
			}
			render();
			frames++;

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + "ups, " + frames + " fps");
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	static Random random = new Random();
	public int time = 0;

	public static int getWWidth() {
		return WIDTH * SCALE;
	}

	public static int getWHeight() {
		return HEIGHT * SCALE;
	}

	public static float volMod = 0;

	public static void playSound(final String url, Float vol) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.class.getResource(url));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(vol + volMod);
			clip.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private int expo = 0;

	public void tick() {

		if (Input.getButton() == 2) {
			zombie = new Zombie(input.mouseX, input.mouseY, level);
			level.add(zombie);
		}
		if (input.volUp) {
			System.out.println(volMod);
			volMod++;
		}
		if (input.volDown) {
			System.out.println(volMod);
			volMod--;
		}
		if (volMod >= 15) volMod = 15;
		if (volMod <= -50) volMod = -50;
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
		double xScroll = (player.getX() - render.width / 2);
		double yScroll = (player.getY() - render.height / 2);
		if (shake != 0) {
			xScroll += random.nextInt(shake);
			yScroll += random.nextInt(shake);
		}
		level.render((int) xScroll, (int) yScroll, render);

		// if (xScroll < 0) xScroll = 0;
		// if (yScroll < 0) yScroll = 0;
		// if (xScroll >= level.width * 16 - render.width - 16) xScroll =
		// level.width * 16 - render.width - 16;
		// if (yScroll >= level.height * 16 - render.height+ 30) yScroll =
		// level.height * 16 - render.height+20;

		if (player.dead) {
			String deathMsg = "YOU ARE DEAD";
			Font.draw(deathMsg, render, WIDTH / 2 - deathMsg.length() * 4, HEIGHT / 2 - 8, 0x5E2727, false);
			Font.draw(deathMsg, render, WIDTH / 2 - deathMsg.length() * 4 - 1, HEIGHT / 2 - 8 - 1, 0xC23434, false);

			String Score = "Score:" + Integer.toString(Player.score);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 + 1 - 15, (HEIGHT / 2) + 4, 0x614B4B, false);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15, (HEIGHT / 2 - 1) + 4, 0xC99797, false);
		} else {
			String Score = "Score:";
			String ScoreNum = Integer.toString(Player.score);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15 + 1, (3), 0x363636, false);
			Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15, (2), 0xEFF589, false);
			Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 1 + 55 - 15, (3), 0x363636, false);
			Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 55- 15, (2), 0xEF358C, false);
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
		// BRIGHTNESS
		if (dayNightCycle) {
			if (alpha >= 100 * 2) alpha = 100 * 2;
			tickCount++;
			if (tickCount % 1000 == 10) alpha++;
			Color col = new Color(10, 10, 10, alpha);
			g.setColor(col);
			g.fillRect(0, 36, getWidth(), getHeight());
		}
		g.dispose();
		bs.show();
	}

	int lvl = 0;

	private void levelTick() {
		if (!menu) {
			if (time % (60 * (random.nextInt(15)+5)) == 0) {
				expo++;
				if (finalLevel) {
					expo += 2;
					for (int i = 0; i < expo - (expo / 3); i++) {
						zombie = new Zombie(level);
						 level.add(zombie);
						 Game.playSound("/sounds/zombie2.wav", -20.0f);

					}
				}
				for (int i = 0; i < expo - (expo / 3); i++) {
					if(ZCount<100) {
					chasingZombie = new ChasingZombie(level);
					level.add(chasingZombie);
					zombie = new Zombie(level);
					 level.add(zombie);
					 if(random.nextInt(10)==2)Game.playSound("/sounds/zombie2.wav", -20.0f);
						ZCount += 2;
					}
					// Game.playSound("/sounds/zombie2.wav", -20.0f);

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
