package Terry.dev.main.level;

import Terry.dev.main.gfx.Render;
import Terry.dev.main.level.tiles.BasementFloorTile;
import Terry.dev.main.level.tiles.GrassCornerBLTile;
import Terry.dev.main.level.tiles.GrassCornerBRTile;
import Terry.dev.main.level.tiles.GrassCornerTLTile;
import Terry.dev.main.level.tiles.GrassCornerTRTile;
import Terry.dev.main.level.tiles.GrassDownTile;
import Terry.dev.main.level.tiles.GrassLeftTile;
import Terry.dev.main.level.tiles.GrassRightTile;
import Terry.dev.main.level.tiles.GrassTile;
import Terry.dev.main.level.tiles.GrassUpTile;
import Terry.dev.main.level.tiles.RottenHeadTile;
import Terry.dev.main.level.tiles.VoidTile;
import Terry.dev.main.level.tiles.WallFrontTile;
import Terry.dev.main.level.tiles.WallIsoTile;
import Terry.dev.main.level.tiles.WallTile;
import Terry.dev.main.level.tiles.WaterTile;
import Terry.dev.main.level.tiles.WoodTile;

public class Tile {

	public static Tile[] tiles = new Tile[120];
	protected int id;

	public static Tile voidTile = new VoidTile(0);
	public static Tile wall = new WallTile(2);
	public static Tile wallFront = new WallFrontTile(3);
	public static Tile wallIso = new WallIsoTile(4);
	public static Tile wood = new WoodTile(5);
	public static Tile water = new WaterTile(6);

	public static Tile grass = new GrassTile(1);
	public static Tile rottenHead = new RottenHeadTile(15);
	public static Tile basementFloor = new BasementFloorTile(16);


	public static Tile grassCornerTL = new GrassCornerTLTile(7);
	public static Tile grassCornerTR = new GrassCornerTRTile(8);
	public static Tile grassCornerBL = new GrassCornerBLTile(9);
	public static Tile grassCornerBR = new GrassCornerBRTile(10);
	public static Tile grassLeft = new GrassLeftTile(11);
	public static Tile grassUp = new GrassUpTile(12);
	public static Tile grassRight = new GrassRightTile(13);
	public static Tile grassDown = new GrassDownTile(14);

	public Tile(int id) {
		this.id = id;
		tiles[id] = this;

	}

	protected void tick() {
	}

	public void render(int x, int y, Render render) {
	}

	public boolean solid() {
		return false;
	}
	
	public boolean Entitysolid() {
		return false;
	}

}
