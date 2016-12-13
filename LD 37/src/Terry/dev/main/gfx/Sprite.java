package Terry.dev.main.gfx;

public class Sprite {

	public int x, y;
	private int width, height;
	public SpriteSheet sheet;
	public int[] pixels;
	public final int SIZE;
	public static final int TSIZE = 16;

	public static Sprite test = new Sprite(16, 2, 0, SpriteSheet.sheet);
	public static Sprite voidSprite = new Sprite(16, 0x2b2b2b);
	public static Sprite particle = new Sprite(3, 2, 0x3B3B3B);
	public static Sprite bloodParticle = new Sprite(2, 3, 0xC43737);

	public static Sprite basementFloor = new Sprite(16, 1, 3, SpriteSheet.sheet);
	public static Sprite rottenHead = new Sprite(16, 3, 0, SpriteSheet.sheet);
	public static Sprite shadow = new Sprite(16, 0, 2, SpriteSheet.sheet);
	public static Sprite wall = new Sprite(16, 1, 0, SpriteSheet.sheet);
	public static Sprite wallFront = new Sprite(16, 0, 1, SpriteSheet.sheet);
	public static Sprite wallIso = new Sprite(16, 0, 0, SpriteSheet.sheet);
	public static Sprite wood = new Sprite(16, 1, 1, SpriteSheet.sheet);

	public static Sprite grass = new Sprite(16, 2, 0, SpriteSheet.sheet);
	public static Sprite grassCorner = new Sprite(16, 4, 0, SpriteSheet.sheet);
	public static Sprite grassLR = new Sprite(16, 4, 1, SpriteSheet.sheet);
	public static Sprite grassUD = new Sprite(16, 5, 0, SpriteSheet.sheet);

	public static Sprite water0 = new Sprite(16, 2, 2, SpriteSheet.sheet);
	public static Sprite water1 = new Sprite(16, 3, 2, SpriteSheet.sheet);
	public static Sprite water2 = new Sprite(16, 4, 2, SpriteSheet.sheet);

	public static Sprite projectile = new Sprite(16, 1, 2, SpriteSheet.sheet);

	public static Sprite playerStillDown = new Sprite(32, 0, 5, SpriteSheet.sheet);
	public static Sprite playerDown1 = new Sprite(32, 0, 6, SpriteSheet.sheet);
	public static Sprite playerDown2 = new Sprite(32, 0, 7, SpriteSheet.sheet);

	public static Sprite playerStillUp = new Sprite(32, 1, 5, SpriteSheet.sheet);
	public static Sprite playerUp1 = new Sprite(32, 1, 6, SpriteSheet.sheet);
	public static Sprite playerUp2 = new Sprite(32, 1, 7, SpriteSheet.sheet);

	public static Sprite playerStillRight = new Sprite(32, 2, 5, SpriteSheet.sheet);
	public static Sprite playerRight1 = new Sprite(32, 2, 6, SpriteSheet.sheet);
	public static Sprite playerRight2 = new Sprite(32, 2, 7, SpriteSheet.sheet);

	public static Sprite zombieStillDown = new Sprite(32, 3, 5, SpriteSheet.sheet);
	public static Sprite zombieDown1 = new Sprite(32, 3, 6, SpriteSheet.sheet);
	public static Sprite zombieDown2 = new Sprite(32, 3, 7, SpriteSheet.sheet);

	public static Sprite zombieStillUp = new Sprite(32, 4, 5, SpriteSheet.sheet);
	public static Sprite zombieUp1 = new Sprite(32, 4, 6, SpriteSheet.sheet);
	public static Sprite zombieUp2 = new Sprite(32, 4, 7, SpriteSheet.sheet);

	public static Sprite zombieStillRight = new Sprite(32, 5, 5, SpriteSheet.sheet);
	public static Sprite zombieRight1 = new Sprite(32, 5, 6, SpriteSheet.sheet);
	public static Sprite zombieRight2 = new Sprite(32, 5, 7, SpriteSheet.sheet);

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.sheet = sheet;
		this.SIZE = size;
		this.x = x * size;
		this.y = y * size;

		pixels = new int[SIZE * SIZE];
		load();
	}

	public Sprite(int width, int height, int color) {
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		getColor(color);
	}

	public Sprite(int size, int color) {
		this.SIZE = size;
		pixels = new int[SIZE * SIZE];
		setColor(color);
	}

	public void setColor(int color) {
		for (int i = 0; i < SIZE * SIZE; i++) {
			pixels[i] = color;
		}
	}

	public void getColor(int color) {
		for (int i = 0; i < width * height; i++) {
			pixels[i] = color;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x + y * SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
			}
		}
	}
}
