package Terry.dev.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;

import Terry.dev.main.entity.CommandCentre;
import Terry.dev.main.entity.DrawerEntity;
import Terry.dev.main.entity.StairEntity;
import Terry.dev.main.entity.mob.ChasingZombie;
import Terry.dev.main.entity.mob.Player;
import Terry.dev.main.entity.mob.Rat;
import Terry.dev.main.entity.mob.Zombie;
import Terry.dev.main.gfx.Font;
import Terry.dev.main.gfx.Render;
import Terry.dev.main.gfx.Sprite;
import Terry.dev.main.gfx.SpriteSheet;
import Terry.dev.main.gui.LootingMenu;
import Terry.dev.main.gui.Menu;
import Terry.dev.main.gui.ShopMenu;
import Terry.dev.main.gui.TitleMenu;
import Terry.dev.main.input.Input;
import Terry.dev.main.level.Level;
import Terry.dev.main.level.OneLevel;
import Terry.dev.main.level.Tile;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 400, HEIGHT = WIDTH / 16 * 11;
	public static int SCALE = 3;
	private JFrame frame;
	private Thread thread;
	private Render render;
	private Player player;
	private Zombie zombie;
	private Rat rat;
	private int tick;
	private ChasingZombie chasingZombie;
	private int alpha = 0;
	public static int cashY = 0;
	public static int ammoY = 0;
	public static boolean firstSpawn = true;
	private Level level;
	public static boolean finalLevel = false;
	public static boolean infiniLevel = false;
	private Input input;
	public boolean paused = false;
	private boolean running = false;
	private boolean canChangeLevel = false;
	private static String title = "Post LD37 - PROJECT Z";
	public static int ZCount = 0;
	private boolean dayNightCycle = false;
	private boolean levelSwitchExecuted = false;

	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private Menu menu;

	public Game() {
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		render = new Render(WIDTH, HEIGHT, pixels, SpriteSheet.tiles_sheet);
		input = new Input(this);
		frame = new JFrame();
		level = new OneLevel("/levels/level1.png");
		setMenu(new TitleMenu());
		player = new Player(input, level);
		level.add(player);
		// rat = new Rat(5 * 16, 2, level);
		// level.add(rat);
		for (int i = 0; i < 0; i++) {
			level.add(new Zombie(level));
		}
	}

	public void getLevel(Level level) {
		this.level = level;
	}

	public void switchLevel() {
		if (!level.cleared) level.prepLevelSwitch();
		level.prepLevelSwitch();
		level = new OneLevel("/levels/level2.png");
		level.cleared = false;
		if (level.cleared) levelSwitchExecuted = true;
		level.levelSwitching = true;
		player = new Player(input, level);
		level.add(player);
		levelSwitchExecuted = true;
	}

	public void restartGame() {
		System.out.println("RESTART GAME");
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
		if (menu != null) menu.init(this, input);
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
		level.save();
		running = false;
		try {
			System.exit(0);
			frame.dispose();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
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

	public static float VOL_MOD = 0;

	public static void playSound(final String url, Float vol) {
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.class.getResource(url));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(vol + VOL_MOD);
			clip.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private int expo = 0;
	private boolean cursorSwitched = false;

	public void tick() {
		if (!(menu instanceof LootingMenu) && DrawerEntity.looting) {
			setMenu(new LootingMenu());
		} else if (!(menu instanceof ShopMenu) && CommandCentre.activated) {
			setMenu(new ShopMenu());
		}

		if (menu instanceof LootingMenu || menu instanceof ShopMenu) {
			level.tick();
			levelTick();

		}

		// if (input.one.clicked && !levelSwitchExecuted) switchLevel();
		if (StairEntity.inRange && !levelSwitchExecuted) switchLevel();

		input.tick();
		if (menu != null) {
			menu.tick();
			if (!cursorSwitched) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Image cursor = toolkit.getImage("res/cursor1.png");
				Point p = new Point((16 / 2) + 1, (16 / 2) + 1);
				Cursor customCursor = toolkit.createCustomCursor(cursor, p, "Cursor");
				setCursor(customCursor);

				cursorSwitched = true;
			}
		} else {

			if (cursorSwitched) {
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				Image cursor = toolkit.getImage("res/cursor.png");
				Point p = new Point((16 / 2) + 1, (16 / 2) + 1);
				Cursor customCursor = toolkit.createCustomCursor(cursor, p, "Cursor");
				setCursor(customCursor);
				cursorSwitched = false;
			}
			if (input.esc.clicked) setMenu(new TitleMenu());
			if (Input.getButton() == 2) {
				zombie = new Zombie(Input.mouseX, Input.mouseY, level);
				level.add(zombie);
			}
			time++;
			level.tick();
			levelTick();
			Tile.tickCount++;
		}
		tick++;
		if (player.addedCash > 0) {

			if (player.cashPickupTime > 0) player.cashPickupTime--;
			if (player.cashPickupTime == 0) {
				cashY = 0;
			}
			if (tick % 2 == 1) cashY++;

		}
		if (player.addedAmmo > 0) {
			if (player.ammoPickupTime > 0) player.ammoPickupTime--;
			if (player.ammoPickupTime == 0) {
				ammoY = 0;
			}
			if (tick % 2 == 1) ammoY++;

		}
	}

	// private BufferedImage main;

	public static int shake;

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
			xScroll -= random.nextInt(shake);
			yScroll += random.nextInt(shake);

		}

		level.render((int) xScroll, (int) yScroll, render);
		renderGui();

		if (menu != null) {
			menu.render(render);
		}

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
			// String Score = "Score:" + Integer.toString(Player.score);
			// Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 + 1 - 15,
			// (HEIGHT / 2) + 4, 0x614B4B, false);
			// Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15,
			// (HEIGHT / 2 - 1) + 4, 0xC99797, false);
		} else if (menu == null && !level.cleared) {
			// String Score = "Score:";
			// String ScoreNum = Integer.toString(Player.score);
			// Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15 + 1,
			// (3), 0x363636, false);
			// Font.draw(Score, render, WIDTH / 2 - Score.length() * 4 - 15,
			// (2), 0xEFF589, false);
			// Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 1 +
			// 55 - 15, (3), 0x363636, false);
			// Font.draw(ScoreNum, render, WIDTH / 2 - Score.length() * 4 + 55 -
			// 15, (2), 0xEF358C, false);
		}

		Graphics g = bs.getDrawGraphics();
		/*
		 * if (menu) { try { main =
		 * ImageIO.read(BufferedImage.class.getResource("/sheets/Main.png")); }
		 * catch (IOException e) { e.printStackTrace(); } } if (menu) { String
		 * msg = "Can anyone hear me?  i need Help! im in the basement!"; String
		 * msg1 =
		 * "Kill all of these zombies to clear the path and please try rescue me!"
		 * ; Font.draw(msg, render, render.width / 2, render.height / 2,
		 * 0xA16468, false); g.drawImage(main, 0, 0, main.getWidth(),
		 * main.getHeight(), null); }
		 */
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		// BRIGHTNESS
		if (dayNightCycle) {
			if (alpha >= 100 * 2) alpha = 100 * 2;
			cashY++;
			if (cashY % 1000 == 10) alpha++;
			Color col = new Color(10, 10, 10, 0);
			g.setColor(col);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if (time < getHeight() && level.levelSwitching) {
			time++;
			g.fillRect(0, time, getWidth(), getHeight());
			level.levelSwitching = false;
		} else {
			time = 0;
		}
		g.dispose();
		bs.show();

	}

	public void renderGui() {
		if (player.hasPistol) {
			render.renderIcon(render.width - 19, render.height - 40, Sprite.pistolIconOff, false, false, false);
			if (player.pistol) {
				render.renderIcon(render.width - 19, render.height - 40, Sprite.pistolIconOn, false, false, false);
			}

		}
		if (player.hasAssaultRifle) {
			render.renderIcon(render.width - 19, render.height - 60, Sprite.assaultRifleIconOff, false, false, false);
			if (player.assaultRifle) {
				render.renderIcon(render.width - 19, render.height - 60, Sprite.assaultRifleIconOn, false, false, false);
			}
		}
		int xx = (int) Math.abs(player.x);
		int yy = (int) Math.abs(player.y);
		Font.draw("Energy:", render, (render.width - 82), render.height - 10, 0x363636, false);
		Font.draw("Energy:", render, (render.width - 82), render.height - 11, 0xEFF589, false);

		Font.draw(Integer.toString(player.energy), render, (render.width - 26), render.height - 10, 0x7E305C, false);
		Font.draw(Integer.toString(player.energy), render, (render.width - 26), render.height - 11, 0xEF358C, false);

		Font.draw("Cash:", render, (render.width - 82), render.height - 20, 0x363636, false);
		Font.draw("Cash:", render, (render.width - 82), render.height - 21, 0xEFF589, false);

		Font.draw(Integer.toString(player.cash), render, (render.width - 42), render.height - 20, 0x7E305C, false);
		Font.draw(Integer.toString(player.cash), render, (render.width - 42), render.height - 21, 0xEF358C, false);

		if (player.addedCash > 0) {
			if (player.cashPickupTime > 0) {
				Font.draw("+" + Integer.toString(player.addedCash), render, (render.width - 115), render.height - (cashY) - 20, 0x7E305C, false);
				Font.draw("+" + Integer.toString(player.addedCash), render, (render.width - 115), render.height - (cashY) - 21, 0xEF358C, false);
			} else {
				player.cashPickupTime = 0;
				player.addedCash = 0;
			}
		}

		if (player.addedAmmo > 0) {
			if (player.ammoPickupTime > 0) {
				Font.draw("+" + Integer.toString(player.addedAmmo), render, (render.width - 50), render.height - (ammoY) - 240, 0x7E305C, false);
				Font.draw("+" + Integer.toString(player.addedAmmo), render, (render.width - 50), render.height - (ammoY) - 241, 0xEF358C, false);
			} else {
				player.ammoPickupTime = 0;
				player.addedAmmo = 0;
			}
		}

		render.renderRect(0, 0, Game.getWWidth(), 13, 0x303030, false);
		render.renderRect(0, 0, Game.getWWidth(), 12, 0x848484, false);
		
		if (player.hasKey) render.renderIcon(WIDTH/2, -3, Sprite.KeyEntity, false, false, false);

		if (player.pistol) {/////////////////////////////////////////
			if (player.PISTOL_CLIP > 0) {

				Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
				Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
			}

			if (player.PISTOL_CLIP <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
					Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 3, 0x5C5E38, false);
					Font.draw(Integer.toString(player.PISTOL_CLIP), render, (render.width - 17), 2, 0xB1B564, false);
				}
			}

			if (player.PISTOL_AMMO <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
					Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x5C5E38, false);
					Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xB1B564, false);
				}
			}
			if (player.PISTOL_AMMO > 0) {
				Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
				Font.draw(Integer.toString(player.PISTOL_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
			}
		}
		if (player.assaultRifle) {//////////////////////////////////////////////
			if (player.ASSAULT_RIFLE_CLIP > 0) {

				Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
				Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
			}

			if (player.ASSAULT_RIFLE_CLIP <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x7E305C, false);
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 3, 0x5C5E38, false);
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_CLIP), render, (render.width - 17), 2, 0xB1B564, false);
				}
			}

			if (player.ASSAULT_RIFLE_AMMO <= 0) {
				if (time % 40 > 20) {
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
				} else {
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x5C5E38, false);
					Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xB1B564, false);
				}
			}
			if (player.ASSAULT_RIFLE_AMMO > 0) {
				Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 3, 0x7E305C, false);
				Font.draw(Integer.toString(player.ASSAULT_RIFLE_AMMO) + ";", render, (render.width - 20) - 25, 2, 0xEF358C, false);
			}
		}

		if (!player.dead && Game.finalLevel) {
			player.finalMessageTime++;
			if (player.finalMessageTime < 10000) {

				String msg = "She is dead! aaaand so am i!";
				Font.draw(msg, render, xx - msg.length() * 4, yy - 30, 0x7E305C, true);
			}
		}

		if (!player.dead && Game.infiniLevel) {
			player.finalMessageTime++;
			if (player.finalMessageTime < 10000) {
				String msg = "you survived! thats surp..ehhmazing!";
				String msg2 = "see how high you can get your score!";
				Font.draw(msg, render, xx - msg.length() * 4, yy - 40, 0x7E305C, true);
				Font.draw(msg2, render, xx - msg.length() * 4 + 2, yy - 30, 0x7E305C, true);
			}
		}

		Font.draw("Ammo:", render, (render.width - 20) - 65, 3, 0x363636, false);
		Font.draw("Ammo:", render, (render.width - 20) - 65, 2, 0xEFF589, false);

		Font.draw("Health:", render, 3, 3, 0x363636, false);
		Font.draw("Health:", render, 2, 2, 0xEFF589, false);

		Font.draw(Integer.toString(player.health), render, 3 + 57, 3, 0x363636, false);
		Font.draw(Integer.toString(player.health), render, 2 + 57, 2, 0xEF358C, false);

	}

	int lvl = 0;

	private void levelTick() {
		if (time % (60 * (random.nextInt(20) + 2)) == 0) {
			expo++;
			expo += 2;
			for (int i = 0; i < expo - (expo / 3); i++) {
				zombie = new Zombie(level);
				level.add(zombie);
				Game.playSound("/sounds/zombie2.wav", -20.0f);

			}
			for (int i = 0; i < expo - (expo / 3); i++) {
				if (ZCount < 40) {
					chasingZombie = new ChasingZombie(level);
					level.add(chasingZombie);
					zombie = new Zombie(level);
					level.add(zombie);
					if (random.nextInt(10) == 2) Game.playSound("/sounds/zombie2.wav", -20.0f);
					ZCount += 2;
				}
				// Game.playSound("/sounds/zombie2.wav", -20.0f);

			}
		}
		if (expo > 5) expo = 0;
		if (canChangeLevel) {

			if (Player.score == 200 && lvl == 0) {
				lvl = 1;
				expo = 0;
				// level.clearLevel();
				System.out.println(0);
				getLevel(new OneLevel("/levels/level2.png"));
				player = new Player(input, level);
				level.add(player);

			}

			if (Player.score == 400 && lvl == 1) {
				lvl = 2;
				// level.clearLevel();
				getLevel(new OneLevel("/levels/level3.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 600 && lvl == 2) {
				lvl = 3;
				// level.clearLevel();
				getLevel(new OneLevel("/levels/level4.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 800 && lvl == 3) {
				lvl = 4;
				// level.clearLevel();
				getLevel(new OneLevel("/levels/level5.png"));
				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 1000 && lvl == 4) {
				lvl = 5;
				// level.clearLevel();
				getLevel(new OneLevel("/levels/levelFinal.png"));
				finalLevel = true;

				player = new Player(input, level);
				level.add(player);
			}

			if (Player.score == 1100 && lvl == 5) {
				lvl = 6;
				// level.clearLevel();
				getLevel(new OneLevel("/levels/level5.png"));
				infiniLevel = true;
				player = new Player(input, level);
				level.add(player);
			}

		}
	}

	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setTitle(title);
		game.frame.setResizable(false);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.start();
	}

}
