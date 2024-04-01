package game;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

	// Screen dimensions
	static final int w = 1200;
	static final int h = 675;

	// rng for random numbers
	static Random rng = new Random();

	// Camera location and angle
	static Camera cam = new Camera(new Point3D(0, 0, 0), 0, 0);

	// Mesh representing the terrain
	static Mesh mesh = genTerrain(-300, 300, -300, 300, -600, 300, 300, 1f, 6);

	// Color scale for terrain
	static final float minHue = 0;
	static final float maxHue = 2;

	// Epsilon for floating point calculations
	static final float epsilon = 1e-9f;

	// Constructor
	public Main() {
		add(new Board());

		setSize(w, h);

		setTitle("3D Engine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	// Main function
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(() -> {
			Main game = new Main();
			game.setVisible(true);
		});
	}

	// Generate mesh terrain using the Diamond Square algorithm
	static Mesh genTerrain(float xmin, float xmax, float zmin, float zmax, float ystart, float yrange, float noise,
			float roughness_factor, int density) {
		int map_size = 1;
		for (int i = 0; i < density; i++) {
			map_size *= 2;
		}
		map_size++;

		// Initialize corner heights of map
		float[][] height_map = new float[map_size][map_size];
		height_map[0][0] = Main.rng.nextFloat() * yrange + ystart;
		height_map[0][map_size - 1] = Main.rng.nextFloat() * yrange + ystart;
		height_map[map_size - 1][0] = Main.rng.nextFloat() * yrange + ystart;
		height_map[map_size - 1][map_size - 1] = Main.rng.nextFloat() * yrange + ystart;

		// Run through square and diamond steps to generate height map
		int chunk_size = map_size - 1;
		while (chunk_size > 1) {
			diamondSquare(height_map, map_size, chunk_size, noise);
			noise *= Math.pow(2, -roughness_factor);
			chunk_size /= 2;
		}

		Point3D[][] points = new Point3D[map_size][map_size];
		float xstep = (xmax - xmin) / (map_size - 1);
		float zstep = (zmax - zmin) / (map_size - 1);
		float ymax = Float.MIN_VALUE;
		float ymin = Float.MAX_VALUE;
		for (int i = 0; i < map_size; i++) {
			for (int j = 0; j < map_size; j++) {
				points[i][j] = new Point3D(xmin + i * xstep, height_map[i][j], zmin + j * zstep);
				ymax = Math.max(ymax, height_map[i][j]);
				ymin = Math.min(ymin, height_map[i][j]);
			}
		}

		// Turn height map into a mesh of triangles
		Mesh mesh = new Mesh(new ArrayList<Triangle>());
		for (int i = 0; i < map_size - 1; i++) {
			for (int j = 0; j < map_size - 1; j++) {
				float height1 = (height_map[i][j] + height_map[i + 1][j] + height_map[i][j + 1]) / 3;
				height1 = (height1 - ymin) / (ymax - ymin);
				float height2 = (height_map[i + 1][j + 1] + height_map[i + 1][j] + height_map[i][j + 1]) / 3;
				height2 = (height2 - ymin) / (ymax - ymin);

				// Color triangles by height
				float hue1 = height1 * maxHue + (1 - height1) * minHue;
				float hue2 = height2 * maxHue + (1 - height2) * minHue;
				Color c1 = new Color(Color.HSBtoRGB(hue1, 1, 0.5f));
				Color c2 = new Color(Color.HSBtoRGB(hue2, 1, 0.5f));

				Triangle tri1 = new Triangle(points[i][j], points[i + 1][j], points[i][j + 1], c1);
				Triangle tri2 = new Triangle(points[i + 1][j + 1], points[i + 1][j], points[i][j + 1], c2);
				mesh.tris.add(tri1);
				mesh.tris.add(tri2);
			}
		}
		return mesh;
	}

	// Performs one iteration of the square step and then diamond step for height
	// map terrain generation
	static void diamondSquare(float[][] height_map, int map_size, int chunk_size, float noise) {
		int half = chunk_size / 2;

		// Square step
		for (int i = 0; i < map_size - 1; i += chunk_size) {
			for (int j = 0; j < map_size - 1; j += chunk_size) {
				height_map[i + half][j + half] = (height_map[i][j] + height_map[i + chunk_size][j]
						+ height_map[i][j + chunk_size] + height_map[i + chunk_size][j + chunk_size]) / 4
						+ (Main.rng.nextFloat() * 2 - 1) * noise;
			}
		}

		// Diamond step
		for (int i = 0; i < map_size; i += half) {
			for (int j = (i + half) % chunk_size; j < map_size; j += chunk_size) {
				int count = 0;
				if (i - half >= 0) {
					height_map[i][j] += height_map[i - half][j];
					count++;
				}
				if (i + half < map_size) {
					height_map[i][j] += height_map[i + half][j];
					count++;
				}
				if (j - half >= 0) {
					height_map[i][j] += height_map[i][j - half];
					count++;
				}
				if (j + half < map_size) {
					height_map[i][j] += height_map[i][j + half];
					count++;
				}
				height_map[i][j] /= count;
			}
		}
	}

	// Signed volume of a tetrahedron (used for collision detection). True if
	// positive, false otherwise.
	static boolean signedVolume(Point3D a, Point3D b, Point3D c, Point3D d) {
		return b.sub(a).cross(c.sub(a)).dot(d.sub(a)) > 0;
	}
}
