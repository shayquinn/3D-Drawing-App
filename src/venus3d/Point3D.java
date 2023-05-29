package venus3d;

import java.awt.Point;

/**
 * The Point3D class represents a point in 3D space with x, y, and z coordinates, and includes methods
 * for creating arrays of points, calculating dot and cross products, and setting and getting the
 * point's coordinates.
 */
public class Point3D {
    private double x, y, z;
    private Point xy;
    
// This is a constructor for the `Point3D` class that takes three double parameters `x`, `y`, and `z`
// representing the coordinates of a point in 3D space. It initializes the `x`, `y`, and `z` properties
// of the `Point3D` object with the corresponding parameter values. It also creates a new `Point`
// object with the `x` and `y` coordinates rounded to the nearest integer and assigns it to the `xy`
// property of the `Point3D` object. This `Point` object represents the projection of the `Point3D`
// object onto the x-y plane.
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xy = new Point((int)x, (int)y);
    }

// The `createPoints` method in the `Point3D` class is creating an array of `Point3D` objects with a
// specified number of points (`num`) and a specified width (`w`). It uses a for loop to iterate
// through 360 degrees, dividing it by the number of points to get the angle increment for each point.
// It then uses a utility method `Utility.convert` to calculate the x and y coordinates for each point
// based on the current angle, the point's y and z coordinates, and the specified width. The resulting
// `Point3D` objects are stored in the `pointArray` array and returned at the end of the method.
    public Point3D[] createPoints(int num, int w, double rx, double ry, double rz) {
        Point3D[] pointArray = new Point3D[num];
        for (int i = 0; i < 360; i += 360 / num) {
            double[] cps = Utility.convert(i, y, z, y, z + w);
            pointArray[i / (360 / num)] = new Point3D(x, Math.round(cps[0]), Math.round(cps[1]));
        }
        return pointArray;
    }// end createPoints
    
//return dot product 
/**
 * This function calculates the dot product of two 3D points.
 * 
 * @param p p is a Point3D object that represents a point in 3D space. It has three properties: x, y,
 * and z, which represent the coordinates of the point along the x, y, and z axes, respectively.
 * @return The method `getDotProduct` returns the dot product of the current `Point3D` object with the
 * `Point3D` object passed as a parameter.
 */
    public double getDotProduct(Point3D p){
        return getX()*p.x + getY()*p.y + getZ()*p.z;
    }
    
    //trturn CrossProduct
/**
 * The function calculates the cross product of two 3D points.
 * 
 * @param p The parameter "p" is a Point3D object that represents the vector to be crossed with the
 * current Point3D object.
 * @return The method `crossProduct` returns a `Point3D` object which represents the cross product of
 * the current `Point3D` object and the `Point3D` object passed as a parameter.
 */
    public Point3D crossProduct(Point3D p) {
        //create normal
        Point3D CrossVector = new Point3D(
                getY() * p.z - getZ() * p.y,
                getZ() * p.x - getX() * p.z,
                getX() * p.y - getY() * p.x
        );
        return CrossVector;
    }
    
// The `setCrossProduct` method in the `Point3D` class takes two `Point3D` objects `u` and `v` as
// parameters and calculates their cross product. It returns a new `Point3D` object with the resulting
// x, y, and z coordinates of the cross product.
    public Point3D setCrossProduct(Point3D u, Point3D v){
        return new Point3D(
                u.y * v.z - u.z * v.y,
                u.z * v.x - u.x * v.z,
                u.x * v.y - u.y * v.x
        );
    }//end setCrossProduct
    

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    
     public Point getXy() {
        return xy;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setXy(Point xy) {
        this.xy = xy;
    }
    

    @Override
    public String toString() {
        return "MyPoint{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

}//end Point3D
