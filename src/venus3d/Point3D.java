package venus3d;

import java.awt.Point;

public class Point3D {
    private double x, y, z;
    private Point xy;
    
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xy = new Point((int)x, (int)y);

    }

    public Point3D[] createPoints(int num, int w, double rx, double ry, double rz) {
        Point3D[] pointArray = new Point3D[num];
        for (int i = 0; i < 360; i += 360 / num) {
            double[] cps = Utility.convert(i, y, z, y, z + w);
            pointArray[i / (360 / num)] = new Point3D(x, Math.round(cps[0]), Math.round(cps[1]));
        }
        return pointArray;
    }// end createPoints
    
    //return dot product 
    public double getDotProduct(Point3D p){
        return getX()*p.x + getY()*p.y + getZ()*p.z;
    }
    
    //trturn CrossProduct
    public Point3D crossProduct(Point3D p) {
        //create normal
        Point3D CrossVector = new Point3D(
                getY() * p.z - getZ() * p.y,
                getZ() * p.x - getX() * p.z,
                getX() * p.y - getY() * p.x
        );
        return CrossVector;
    }
    
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

}
