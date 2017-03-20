package Terry.dev.main.gfx;

public class Sprite {

	public int x, y;
	private int width, height;
	public SpriteSheet sheet;
	public int[] pixels;
	public final int SIZE;
	public static final int TSIZE = 16;

	public static Sprite test = new Sprite(16, 2, 0, SpriteSheet.tiles_sheet);
	public static Sprite voidSprite = new Sprite(16, 0x2b2b2b);
	public static Sprite particle = new Sprite(3, 2, 0x3B3B3B);
	public static Sprite casingParticle = new Sprite(3, 1, 0xB59900);
	public static Sprite bloodParticle = new Sprite(2, 2, 0x4E1616);

	public static Sprite guiCorner = new Sprite(16, 0, 7, SpriteSheet.tiles_sheet);
	public static Sprite guiTop = new Sprite(16, 1, 7, SpriteSheet.tiles_sheet);
	public static Sprite guiFull = new Sprite(16, 1, 8, SpriteSheet.tiles_sheet);
	public static Sprite guiSide = new Sprite(16, 0, 8, SpriteSheet.tiles_sheet);

	public static Sprite command_centre_on = new Sprite(16, 6, 0, SpriteSheet.tiles_sheet);
	public static Sprite command_centre_flash = new Sprite(16, 7, 0, SpriteSheet.tiles_sheet);
	public static Sprite command_centre_off = new Sprite(16, 8, 0, SpriteSheet.tiles_sheet);
	public static Sprite command_centre_shadow = new Sprite(16, 6, 1, SpriteSheet.tiles_sheet);
	public static Sprite command_centre_legs = new Sprite(16, 7, 1, SpriteSheet.tiles_sheet);
	public static Sprite f_indicator = new Sprite(16, 9, 0, SpriteSheet.tiles_sheet);

	public static Sprite drawer = new Sprite(16, 10, 0, SpriteSheet.tiles_sheet);
	
	public static Sprite selector = new Sprite(16, 0, 3, SpriteSheet.tiles_sheet);
	public static Sprite spikes = new Sprite(16, 1, 3, SpriteSheet.tiles_sheet);
	public static Sprite spikesBlood = new Sprite(16, 1, 4, SpriteSheet.tiles_sheet);

	public static Sprite spikeIcon = new Sprite(16, 2, 3, SpriteSheet.tiles_sheet);
	public static Sprite pistolIconOff = new Sprite(16, 3, 3, SpriteSheet.tiles_sheet);
	public static Sprite assaultRifleIconOff = new Sprite(16, 4, 3, SpriteSheet.tiles_sheet);
	
	public static Sprite pistolIconOn = new Sprite(16, 3, 4, SpriteSheet.tiles_sheet);
	public static Sprite assaultRifleIconOn = new Sprite(16, 4, 4, SpriteSheet.tiles_sheet);

	public static Sprite CashEntity = new Sprite(16, 5, 3, SpriteSheet.tiles_sheet);
	public static Sprite AmmoEntity = new Sprite(16, 6, 3, SpriteSheet.tiles_sheet);
	
	public static Sprite rottenHead = new Sprite(16, 3, 0, SpriteSheet.tiles_sheet);
	public static Sprite rottenArm = new Sprite(16, 11, 0, SpriteSheet.tiles_sheet);
	public static Sprite blood = new Sprite(16, 12, 0, SpriteSheet.tiles_sheet);

	public static Sprite shadow = new Sprite(16, 0, 2, SpriteSheet.tiles_sheet);
	public static Sprite wall = new Sprite(16, 1, 0, SpriteSheet.tiles_sheet);
	public static Sprite wallFront = new Sprite(16, 0, 1, SpriteSheet.tiles_sheet);
	public static Sprite wallIso = new Sprite(16, 0, 0, SpriteSheet.tiles_sheet);
	public static Sprite wood = new Sprite(16, 1, 1, SpriteSheet.tiles_sheet);

	public static Sprite stone = new Sprite(16, 5, 1, SpriteSheet.tiles_sheet);
	
	public static Sprite bridgeWallTop = new Sprite(16, 5, 2, SpriteSheet.tiles_sheet);
	public static Sprite bridgeWallBottom = new Sprite(16, 6, 2, SpriteSheet.tiles_sheet);

	public static Sprite flowers = new Sprite(16, 3, 1, SpriteSheet.tiles_sheet);
	public static Sprite grass_Snow = new Sprite(16, 2, 1, SpriteSheet.tiles_sheet);
	public static Sprite grass = new Sprite(16, 2, 0, SpriteSheet.tiles_sheet);
	public static Sprite grassCorner = new Sprite(16, 4, 0, SpriteSheet.tiles_sheet);
	public static Sprite grassLR = new Sprite(16, 4, 1, SpriteSheet.tiles_sheet);
	public static Sprite grassUD = new Sprite(16, 5, 0, SpriteSheet.tiles_sheet);

	public static Sprite water0 = new Sprite(16, 2, 2, SpriteSheet.tiles_sheet);
	public static Sprite water1 = new Sprite(16, 3, 2, SpriteSheet.tiles_sheet);
	public static Sprite water2 = new Sprite(16, 4, 2, SpriteSheet.tiles_sheet);

	public static Sprite projectile = new Sprite(16, 1, 2, SpriteSheet.tiles_sheet);

	// PLAYER WITH PISTOL
	public static Sprite playerStillDown = new Sprite(32, 0, 0, SpriteSheet.player_sheet);
	public static Sprite playerDown1 = new Sprite(32, 0, 1, SpriteSheet.player_sheet);
	public static Sprite playerDown2 = new Sprite(32, 0, 2, SpriteSheet.player_sheet);

	public static Sprite playerStillUp = new Sprite(32, 1, 0, SpriteSheet.player_sheet);
	public static Sprite playerUp1 = new Sprite(32, 1, 1, SpriteSheet.player_sheet);
	public static Sprite playerUp2 = new Sprite(32, 1, 2, SpriteSheet.player_sheet);

	public static Sprite playerStillRight = new Sprite(32, 2, 0, SpriteSheet.player_sheet);
	public static Sprite playerRight1 = new Sprite(32, 2, 1, SpriteSheet.player_sheet);
	public static Sprite playerRight2 = new Sprite(32, 2, 2, SpriteSheet.player_sheet);

	// PLAYER WITH AR
	public static Sprite AR_playerStillDown = new Sprite(32, 0, 3, SpriteSheet.player_sheet);
	public static Sprite AR_playerDown1 = new Sprite(32, 0, 4, SpriteSheet.player_sheet);
	public static Sprite AR_playerDown2 = new Sprite(32, 0, 5, SpriteSheet.player_sheet);

	public static Sprite AR_playerStillUp = new Sprite(32, 1, 3, SpriteSheet.player_sheet);
	public static Sprite AR_playerUp1 = new Sprite(32, 1, 4, SpriteSheet.player_sheet);
	public static Sprite AR_playerUp2 = new Sprite(32, 1, 5, SpriteSheet.player_sheet);

	public static Sprite AR_playerStillRight = new Sprite(32, 2, 3, SpriteSheet.player_sheet);
	public static Sprite AR_playerRight1 = new Sprite(32, 2, 4, SpriteSheet.player_sheet);
	public static Sprite AR_playerRight2 = new Sprite(32, 2, 5, SpriteSheet.player_sheet);

	// PLAYER WITHOUT GUN
	public static Sprite disarmed_playerStillDown = new Sprite(32, 4, 0, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerDown1 = new Sprite(32, 4, 1, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerDown2 = new Sprite(32, 4, 2, SpriteSheet.player_sheet);

	public static Sprite disarmed_playerStillUp = new Sprite(32, 5, 0, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerUp1 = new Sprite(32, 5, 1, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerUp2 = new Sprite(32, 5, 2, SpriteSheet.player_sheet);

	public static Sprite disarmed_playerStillRight = new Sprite(32, 6, 0, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerRight1 = new Sprite(32, 6, 1, SpriteSheet.player_sheet);
	public static Sprite disarmed_playerRight2 = new Sprite(32, 6, 2, SpriteSheet.player_sheet);

	public static Sprite zombieStillDown = new Sprite(32, 0, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown1 = new Sprite(32, 0, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown2 = new Sprite(32, 0, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillUp = new Sprite(32, 1, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp1 = new Sprite(32, 1, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp2 = new Sprite(32, 1, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillRight = new Sprite(32, 2, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight1 = new Sprite(32, 2, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight2 = new Sprite(32, 2, 2, SpriteSheet.zombie_sheet);

	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		this.sheet = sheet;
		this.SIZE = size;
		this.x = x * size;
		this.y = y * size;
		this.width = SIZE;
		this.height = SIZE;
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
		this.width = SIZE;
		this.height = SIZE;
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
