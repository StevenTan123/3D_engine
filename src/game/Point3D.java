package game;

public class Point3D {
    float x, y, z;

    Point3D(float xx, float yy, float zz) {
        x = xx;
        y = yy;
        z = zz;
    }
    
    // Return the point obtained by adding this to vector a
    Point3D add(Point3D a) {
    	return new Point3D(x + a.x, y + a.y, z + a.z);
    }
    
    // Return the vector obtained by subtracting vector a from this
    Point3D sub(Point3D a) {
    	return new Point3D(x - a.x, y - a.y, z - a.z);
    }
    
    // Return the vector obtained by scaling this vector by a factor f
    Point3D scale(float f) {
    	return new Point3D(x * f, y * f, z * f);
    }
    
    // Dot product with a
    float dot(Point3D a) {
    	return x * a.x + y * a.y + z * a.z;
    }
    
    // Cross product with a
    Point3D cross(Point3D a) {
    	return new Point3D(y * a.z - z * a.y, 
    					   z * a.x - x * a.z,
    					   x * a.y - y * a.x);
    }
    
    // Rotate around point a
    Point3D rotAround(Point3D a, float ax, float ay, float az) {
        return sub(a).rotX(ax).rotY(ay).rotZ(az).add(a);
    }

    // Rotate point given rotation matrix
    Point3D rotPoint(float[][] rotMat) {
        float[] b = new float[3];
        for (int i = 0; i < 3; i++) {
            b[i] = rotMat[i][0] * x + rotMat[i][1] * y + rotMat[i][2] * z;
        }
        return new Point3D(b[0], b[1], b[2]);
    }

    // Rotate point around x-axis, counter-clockwise
    Point3D rotX(float ang) {
        float cos = (float) Math.cos(ang);
        float sin = (float) Math.sin(ang);
        float[][] rotMat = new float[][] {
            {1, 0, 0},
            {0, cos, -sin},
            {0, sin, cos}
        };
        return rotPoint(rotMat);
    }

    // Rotate point around y-axis, counter-clockwise
    Point3D rotY(float ang) {
        float cos = (float) Math.cos(ang);
        float sin = (float) Math.sin(ang);
        float[][] rotMat = new float[][] {
            { cos, 0, sin },
            { 0, 1, 0 },
            { -sin, 0, cos }
        };
        return rotPoint(rotMat);
    }
    
    // Rotate point around z-axis, counter-clockwise
    Point3D rotZ(float ang) {
        float cos = (float) Math.cos(ang);
        float sin = (float) Math.sin(ang);
        float[][] rotMat = new float[][] {
            { cos, -sin, 0 },
            { sin, cos, 0 },
            { 0, 0, 1 }
        };
        return rotPoint(rotMat);
    }
    
    float dist(Point3D o) {
        return (float) Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y) + (z - o.z) * (z - o.z));
    }
}
