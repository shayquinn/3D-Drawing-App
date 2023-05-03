package venus3d;

import java.awt.Color;
import java.awt.Point;

public class Polygon3D {

    private Point3D p1, p2, p3;
    private Point p2d1, p2d2, p2d3;
    private Color c;
    private boolean frame;
    

    public Polygon3D(Point3D p1, Point3D p2, Point3D p3, Color c, boolean frame) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.c = c;
        this.frame = frame;
    }

    public Polygon3D(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.c = Color.BLUE;
    }

    public Polygon3D(Point3D[] p, Color c, boolean frame) {
        this.p1 = p[0];
        this.p2 = p[1];
        this.p3 = p[2];
        this.c = c;
        this.frame = frame;
    }

    public Polygon3D(Point3D[] p) {
        this.p1 = p[0];
        this.p2 = p[1];
        this.p3 = p[2];
        this.c = Color.BLUE;
    }

    public Point3D centroid() {
        double sx = p1.getX();
        double sy = p1.getY();
        double sz = p1.getZ();
        sx += p2.getX();
        sy += p2.getY();
        sz += p2.getZ();     
        sx += p3.getX();
        sy += p3.getY();
        sz += p3.getZ();
        return new Point3D(sx / 3, sy / 3, sz / 3);
    }
    
    public Point3D calcNormal(){
        Point3D U = new Point3D(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
	Point3D V = new Point3D(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
	return new Point3D(
                U.getY()*V.getZ() - U.getZ()*V.getY(),
                U.getZ()*V.getX() - U.getX()*V.getZ(),
                U.getX()*V.getY() - U.getY()*V.getX()
        );
    }

    public Point getP2d1() {
        return p2d1;
    }

    public Point getP2d2() {
        return p2d2;
    }

    public Point getP2d3() {
        return p2d3;
    }

    public Point3D getP1() {
        return p1;
    }

    public Point3D getP2() {
        return p2;
    }

    public Point3D getP3() {
        return p3;
    }

    public Color getC() {
        return c;
    }

    public boolean isFrame() {
        return frame;
    }

    public void setP1(Point3D p1) {
        this.p1 = p1;
    }

    public void setP2(Point3D p2) {
        this.p2 = p2;
    }

    public void setP3(Point3D p3) {
        this.p3 = p3;
    }

    public void setC(Color c) {
        this.c = c;
    }

    public void setP2d1(Point p2d1) {
        this.p2d1 = p2d1;
    }

    public void setP2d2(Point p2d2) {
        this.p2d2 = p2d2;
    }

    public void setP2d3(Point p2d3) {
        this.p2d3 = p2d3;
    }
    
    

    public void setFrame(boolean frame) {
        this.frame = frame;
    }

    @Override
    public String toString() {
        return "Polygon3D{" + "p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + ", c=" + c + '}';
    }

}
