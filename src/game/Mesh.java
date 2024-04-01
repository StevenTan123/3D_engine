package game;

import java.util.*;

public class Mesh {
    ArrayList<Triangle> tris;

    Mesh(ArrayList<Triangle> t) {
        tris = t;
    }
    
    // Returns negative value if line segment does not intersect with this mesh,
    // otherwise returns value between 0 and 1 indicating intersection point along line segment
    float segmentIntersection(Point3D q1, Point3D q2) {
    	for (Triangle tri : tris) {
    		float intersection = tri.segmentIntersection(q1, q2);
    		if (intersection >= 0) {
    			return intersection;
    		}
    	}
    	return -1;
    }
}
