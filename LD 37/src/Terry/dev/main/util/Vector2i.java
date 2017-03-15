package Terry.dev.main.util;

public class Vector2i {

	public int x, y;

	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2i(Vector2i vector) {
		this.x = vector.x;
		this.y = vector.y;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2i vector) {
		this.x += vector.x;
		this.y += vector.y;
	}

	public void subtract(Vector2i vector) {
		this.x -= vector.x;
		this.y -= vector.y;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if (vec.x == (int)this.x && vec.y == (int)this.y) return true;
		return false;
	}
}
