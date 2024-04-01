package game;

public class Camera {

	// Location of the camera
	Point3D loc;

	// Camera is a vertical stick with a height, loc refers to the top of the stick
	final float height = 40;
	final float gravity = -0.08f;

	// Angles about each axis
	float ax, ay;

	// FOV, width, height, distance to projection plane, render distance
	final float FOV = 90;
	final float VIEW_WIDTH = 2;
	final float VIEW_HEIGHT = 2;
	final float VIEW_DIST = (float) (1 / Math.tan(FOV / 2) * VIEW_WIDTH / 2);

	// Move, jump, rotation speeds
	final float MOVE_SPEED = 1f;
	final float JUMP_SPEED = 3f;
	final float X_ROT_SPEED = 0.02f;
	final float Y_ROT_SPEED = 0.03f;

	// Currently moving speed
	float velf, vels, vely;

	// Current changing angles
	float dah, dav;

	// Takes the point and 3 angles
	Camera(Point3D l, float x, float y) {
		loc = l;
		ax = x;
		ay = y;
	}

	// Rotates point to coordinate system of camera
	Point3D cameraRot(Point3D a) {
		return a.sub(loc).rotY(-ay).rotX(-ax);
	}

	// Projects point onto screen, returns null if behind screen
	Point2D projectPoint(Point3D a) {
		Point3D rot = cameraRot(a);
		if (rot.z > 0) {
			// Screen space coordinates
			float projx = rot.x * VIEW_DIST / rot.z;
			float projy = rot.y * VIEW_DIST / rot.z;

			// Scaling from screen space to display space
			float screenx = (projx + VIEW_WIDTH / 2) * Main.w / VIEW_WIDTH;
			float screeny = Main.h - (projy + VIEW_HEIGHT / 2) * Main.h / VIEW_HEIGHT;

			return new Point2D(screenx, screeny);
		}
		return null;
	}

	// Moves camera according to player inputs in current frame
	void move() {
		ax += dav;
		ay += dah;
		vely += gravity;

		float newx = (float) (loc.x + velf * Math.sin(ay) + vels * Math.cos(ay));
		float newy = (float) (loc.y + vely);
		float newz = (float) (loc.z + velf * Math.cos(ay) - vels * Math.sin(ay));

		Point3D above = new Point3D(newx, newy, newz);
		Point3D below = new Point3D(newx, newy - height, newz);

		float intersection = Main.mesh.segmentIntersection(above, below);
		if (intersection >= 0 && intersection <= 0.8) {
			vely = 0;
		} else {
			loc.y = newy;
			// If barely colliding with triangle, move player above triangle, allows walking
			// up slopes
			if (intersection >= 0) {
				vely = 0;
				loc.y += (1 - intersection) * height;
			}
			loc.x = newx;
			loc.z = newz;
		}
	}
}
