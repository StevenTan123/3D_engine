package game;

import java.awt.Color;

public class Triangle {
	Point3D[] pts;
    Point3D centroid;

    Color c;

    Triangle(Point3D a, Point3D b, Point3D c) {
        pts = new Point3D[] { a, b, c };
        centroid = new Point3D ((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3);
        this.c = new Color(Main.rng.nextFloat(), Main.rng.nextFloat(), Main.rng.nextFloat());
    }
    
    Triangle(Point3D a, Point3D b, Point3D c, Color col) {
        pts = new Point3D[] { a, b, c };
        centroid = new Point3D ((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3, (a.z + b.z + c.z) / 3);
        this.c = col;
    }
    
    // Returns negative value if line segment does not intersect, otherwise returns 
    // value between 0 and 1 indicating intersection point along line segment
    float segmentIntersection(Point3D q1, Point3D q2) {
    	Point3D p1 = pts[0];
    	Point3D p2 = pts[1];
    	Point3D p3 = pts[2];
    	
    	if (Main.signedVolume(q1, p1, p2, p3) != Main.signedVolume(q2, p1, p2, p3)) {
    		boolean sign = Main.signedVolume(q1, q2, p1, p2);
    		if (sign == Main.signedVolume(q1, q2, p2, p3) && sign == Main.signedVolume(q1, q2, p3, p1)) {
    			Point3D perp = p2.sub(p1).cross(p3.sub(p1));
    			float intersection = -q1.sub(p1).dot(perp) / q2.sub(q1).dot(perp);
    			return intersection;
    		}
    	}
    	return -1;
    }
}
