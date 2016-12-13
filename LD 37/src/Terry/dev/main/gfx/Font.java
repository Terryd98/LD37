package Terry.dev.main.gfx;

public class Font {

	private static String chars = "" + //
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
			"0123456789.,!?'\"-+=/\\%()<>:;    " + //
			"";

	public static void draw(String msg, Render render, int x, int y, int col, boolean following) {
		msg = msg.toUpperCase();
		for (int i = 0; i < msg.length(); i++) {
			int ix = chars.indexOf(msg.charAt(i));
			if (ix >= 0) {
				render.render(x + i * 8, y, ix + 18 * 32, col, false, false, following);
			}
		}
	}
}
