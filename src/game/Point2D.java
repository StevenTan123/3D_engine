package game;

public class Point2D {
	float x, y;

	Point2D(float xx, float yy) {
		x = xx;
		y = yy;
	}

	// Returns distance to another 2D point
	float dist(Point2D o) {
		return (float) Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y));
	}

	// Returns if this point is in screen
	boolean inScreen() {
		if (y >= 0 && y < Main.HEIGHT && x >= 0 && x < Main.WIDTH) {
			return true;
		}
		return false;
	}
}
