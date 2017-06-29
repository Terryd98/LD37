package Terry.dev.main.gfx;

public class Sprite {

	public int x, y;
	public int width, height;
	public SpriteSheet sheet;
	public int[] pixels;
	public final int SIZE;
	public static int T_SIZE = 16;

	public static Sprite title = new Sprite(224, 72, 0, 0, SpriteSheet.ui_sheet);
	public static Sprite title1 = new Sprite(224, 72, 0, 1, SpriteSheet.ui_sheet);
	public static Sprite title2 = new Sprite(224, 72, 0, 2, SpriteSheet.ui_sheet);
	public static Sprite title3 = new Sprite(224, 72, 0, 3, SpriteSheet.ui_sheet);

	public static Sprite tallTreeA = new Sprite(32, 64, 0, 0, SpriteSheet.big_tiles);
	public static Sprite tallTreeB = new Sprite(32, 64, 0, 1, SpriteSheet.big_tiles);
	public static Sprite tallTreeC = new Sprite(32, 64, 0, 2, SpriteSheet.big_tiles);

	public static Sprite tallTree1A = new Sprite(32, 64, 1, 0, SpriteSheet.big_tiles);
	public static Sprite tallTree1B = new Sprite(32, 64, 1, 1, SpriteSheet.big_tiles);
	public static Sprite tallTree1C = new Sprite(32, 64, 1, 2, SpriteSheet.big_tiles);

	public static Sprite treeTrunk = new Sprite(16, 9, 2, SpriteSheet.tiles_sheet);
	public static Sprite treeTrunk1 = new Sprite(16, 10, 2, SpriteSheet.tiles_sheet);
	public static Sprite logParticle1 = new Sprite(16, 11, 2, SpriteSheet.tiles_sheet);
	public static Sprite logParticle = new Sprite(16, 12, 2, SpriteSheet.tiles_sheet);
	public static Sprite sapling = new Sprite(16, 11, 3, SpriteSheet.tiles_sheet);

	public static Sprite stairs = new Sprite(16, 13, 2, SpriteSheet.tiles_sheet);
	public static Sprite Vault_Unconnected = new Sprite(16, 14, 2, SpriteSheet.tiles_sheet);
	public static Sprite VaultDoor = new Sprite(16 * 5, 0, 0, SpriteSheet.basement_tiles_sheet);
	public static Sprite VaultDoor_opening = new Sprite(16 * 5, 0, 1, SpriteSheet.basement_tiles_sheet);
	public static Sprite VaultDoor_open = new Sprite(16 * 5, 0, 2, SpriteSheet.basement_tiles_sheet);

	public static Sprite test = new Sprite(16, 2, 0, SpriteSheet.tiles_sheet);
	public static Sprite voidSprite = new Sprite(16, 0x2b2b2b);
	public static Sprite particle = new Sprite(3, 2, 0x4B2929);
	public static Sprite tntParticle = new Sprite(3, 2, 0xa41e22);
	public static Sprite casingParticle = new Sprite(3, 1, 0xB59900);
	public static Sprite bloodParticle = new Sprite(2, 2, 0x4E1616);

	public static Sprite guiCorner = new Sprite(16, 0, 7, SpriteSheet.tiles_sheet);
	public static Sprite guiTop = new Sprite(16, 1, 7, SpriteSheet.tiles_sheet);
	public static Sprite guiSelector = new Sprite(16, 1, 8, SpriteSheet.tiles_sheet);
	public static Sprite guiFull = new Sprite(16, 2, 8, SpriteSheet.tiles_sheet);
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
	public static Sprite GrenadeEntity = new Sprite(16, 7, 3, SpriteSheet.tiles_sheet);
	public static Sprite KeyEntity = new Sprite(16, 5, 4, SpriteSheet.tiles_sheet);

	public static Sprite workTable = new Sprite(16, 6, 4, SpriteSheet.tiles_sheet);

	public static Sprite rottenHead = new Sprite(16, 3, 0, SpriteSheet.tiles_sheet);
	public static Sprite rottenArm = new Sprite(16, 11, 0, SpriteSheet.tiles_sheet);
	public static Sprite blood = new Sprite(16, 12, 0, SpriteSheet.tiles_sheet);
	public static Sprite blood_drop = new Sprite(16, 13, 0, SpriteSheet.tiles_sheet);

	public static Sprite shadow = new Sprite(16, 0, 2, SpriteSheet.tiles_sheet);
	public static Sprite wall = new Sprite(16, 1, 0, SpriteSheet.tiles_sheet);
	public static Sprite wallFront = new Sprite(16, 0, 1, SpriteSheet.tiles_sheet);
	public static Sprite wallIso = new Sprite(16, 0, 0, SpriteSheet.tiles_sheet);
	public static Sprite wood = new Sprite(16, 1, 1, SpriteSheet.tiles_sheet);

	public static Sprite stone = new Sprite(16, 5, 1, SpriteSheet.tiles_sheet);

	public static Sprite bridgeWallTop = new Sprite(16, 5, 2, SpriteSheet.tiles_sheet);
	public static Sprite bridgeWallBottom = new Sprite(16, 6, 2, SpriteSheet.tiles_sheet);

	public static Sprite bin = new Sprite(16, 7, 2, SpriteSheet.tiles_sheet);

	public static Sprite bushA = new Sprite(16, 8, 2, SpriteSheet.tiles_sheet);
	public static Sprite bushB = new Sprite(16, 8, 3, SpriteSheet.tiles_sheet);

	public static Sprite flowerA = new Sprite(16, 3, 1, SpriteSheet.tiles_sheet);
	public static Sprite flowerB = new Sprite(16, 2, 1, SpriteSheet.tiles_sheet);

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

	// SWIMMING
	public static Sprite playerStillDown_Swimming = new Sprite(32, 4, 3, SpriteSheet.player_sheet);
	public static Sprite playerDown1_Swimming = new Sprite(32, 4, 4, SpriteSheet.player_sheet);
	public static Sprite playerDown2_Swimming = new Sprite(32, 4, 5, SpriteSheet.player_sheet);

	public static Sprite playerStillUp_Swimming = new Sprite(32, 5, 3, SpriteSheet.player_sheet);
	public static Sprite playerUp1_Swimming = new Sprite(32, 5, 4, SpriteSheet.player_sheet);
	public static Sprite playerUp2_Swimming = new Sprite(32, 5, 5, SpriteSheet.player_sheet);

	public static Sprite playerStillRight_Swimming = new Sprite(32, 6, 3, SpriteSheet.player_sheet);
	public static Sprite playerRight1_Swimming = new Sprite(32, 6, 4, SpriteSheet.player_sheet);
	public static Sprite playerRight2_Swimming = new Sprite(32, 6, 5, SpriteSheet.player_sheet);

	public static Sprite splashA = new Sprite(32, 3, 3, SpriteSheet.player_sheet);
	public static Sprite splashB = new Sprite(32, 3, 4, SpriteSheet.player_sheet);
	public static Sprite splashC = new Sprite(32, 3, 5, SpriteSheet.player_sheet);

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
	/////////////////////////////
	public static Sprite zombieStillDown_bloody = new Sprite(32, 0, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown1_bloody = new Sprite(32, 0, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown2_bloody = new Sprite(32, 0, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillUp_bloody = new Sprite(32, 1, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp1_bloody = new Sprite(32, 1, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp2_bloody = new Sprite(32, 1, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillRight_bloody = new Sprite(32, 2, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight1_bloody = new Sprite(32, 2, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight2_bloody = new Sprite(32, 2, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillDownA_bloody = new Sprite(32, 3, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown1A_bloody = new Sprite(32, 3, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown2A_bloody = new Sprite(32, 3, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillUpA_bloody = new Sprite(32, 4, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp1A_bloody = new Sprite(32, 4, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp2A_bloody = new Sprite(32, 4, 2, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillRightA_bloody = new Sprite(32, 5, 0, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight1A_bloody = new Sprite(32, 5, 1, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight2A_bloody = new Sprite(32, 5, 2, SpriteSheet.zombie_sheet);

	////////////////////////////////////

	public static Sprite zombieStillDown = new Sprite(32, 0, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown1 = new Sprite(32, 0, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown2 = new Sprite(32, 0, 5, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillUp = new Sprite(32, 1, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp1 = new Sprite(32, 1, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp2 = new Sprite(32, 1, 5, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillRight = new Sprite(32, 2, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight1 = new Sprite(32, 2, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight2 = new Sprite(32, 2, 5, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillDownA = new Sprite(32, 3, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown1A = new Sprite(32, 3, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieDown2A = new Sprite(32, 3, 5, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillUpA = new Sprite(32, 4, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp1A = new Sprite(32, 4, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieUp2A = new Sprite(32, 4, 5, SpriteSheet.zombie_sheet);

	public static Sprite zombieStillRightA = new Sprite(32, 5, 3, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight1A = new Sprite(32, 5, 4, SpriteSheet.zombie_sheet);
	public static Sprite zombieRight2A = new Sprite(32, 5, 5, SpriteSheet.zombie_sheet);
	///////////////////

	public static Sprite ratStillDown = new Sprite(32, 0, 0, SpriteSheet.rat_sheet);
	public static Sprite ratDown1 = new Sprite(32, 0, 1, SpriteSheet.rat_sheet);
	public static Sprite ratDown2 = new Sprite(32, 0, 2, SpriteSheet.rat_sheet);

	public static Sprite ratStillUp = new Sprite(32, 1, 0, SpriteSheet.rat_sheet);
	public static Sprite ratUp1 = new Sprite(32, 1, 1, SpriteSheet.rat_sheet);
	public static Sprite ratUp2 = new Sprite(32, 1, 2, SpriteSheet.rat_sheet);

	public static Sprite ratStillRight = new Sprite(32, 2, 0, SpriteSheet.rat_sheet);
	public static Sprite ratRight1 = new Sprite(32, 2, 1, SpriteSheet.rat_sheet);
	public static Sprite ratRight2 = new Sprite(32, 2, 2, SpriteSheet.rat_sheet);

	// BASEMENT TILES
	public static Sprite floor = new Sprite(16, 0, 0, SpriteSheet.basement_tiles_sheet);

	// BOAT
	public static Sprite boat = new Sprite(2 * 32, 1 * 32, 0, 0, SpriteSheet.boat_sheet);
	public static Sprite boat1 = new Sprite(2 * 32, 1 * 32, 0, 1, SpriteSheet.boat_sheet);
	public static Sprite paddleLR_0 = new Sprite(1 * 32, 1 * 32, 2, 0, SpriteSheet.boat_sheet);
	public static Sprite paddleLR_1 = new Sprite(1 * 32, 1 * 32, 2, 1, SpriteSheet.boat_sheet);
	public static Sprite paddleUD_0 = new Sprite(1 * 32, 1 * 32, 3, 0, SpriteSheet.boat_sheet);
	public static Sprite paddleUD_1 = new Sprite(1 * 32, 1 * 32, 3, 1, SpriteSheet.boat_sheet);

	public static Sprite playerPaddleDown = new Sprite(32, 7, 0, SpriteSheet.player_sheet);
	public static Sprite playerPaddleDown1 = new Sprite(32, 7, 1, SpriteSheet.player_sheet);
	public static Sprite playerPaddleDown2 = new Sprite(32, 7, 2, SpriteSheet.player_sheet);

	public static Sprite playerPaddleUp = new Sprite(32, 8, 0, SpriteSheet.player_sheet);
	public static Sprite playerPaddleUp1 = new Sprite(32, 8, 1, SpriteSheet.player_sheet);
	public static Sprite playerPaddleUp2 = new Sprite(32, 8, 2, SpriteSheet.player_sheet);

	public static Sprite playerPaddleRight = new Sprite(32, 9, 0, SpriteSheet.player_sheet);
	public static Sprite playerPaddleRight1 = new Sprite(32, 9, 1, SpriteSheet.player_sheet);
	public static Sprite playerPaddleRight2 = new Sprite(32, 9, 2, SpriteSheet.player_sheet);

	// public static Sprite boatN = new Sprite(2 * 32, 5 * 32, 3, 0,
	// SpriteSheet.boat_sheet);
	// public static Sprite boatS = new Sprite(2 * 32, 5 * 32, 4, 0,
	// SpriteSheet.boat_sheet);
	// public static Sprite boatNW = new Sprite(5 * 32, 2 * 32, 0, 0,
	// SpriteSheet.boat_sheet);

	// public static Sprite boat1 = new Sprite(5 * 32, 2 * 32, 0, 1,
	// SpriteSheet.boat_sheet);
	// public static Sprite boat2 = new Sprite(5 * 32, 2 * 32, 0, 2,
	// SpriteSheet.boat_sheet);

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

	public Sprite(int width, int height, int x, int y, SpriteSheet sheet) {
		this.sheet = sheet;
		this.SIZE = -1;
		this.x = x * width;
		this.y = y * height;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		loadWH();
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

	private void loadWH() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
			}
		}
	}
}
