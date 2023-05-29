
package venus3d;

import java.awt.Color;
import java.awt.geom.Point2D;

public class VenusPoint {

    private String name;
    private Point2D.Double p;
    private Color sc;
    private Color fc;
    private int r;
    private boolean sel;

    // This is a class called `VenusPoint` that represents a point in a 2D space. It has several
    // properties such as name, position (represented by a `Point2D.Double` object), stroke color, fill
    // color, radius, and selection status. The constructor initializes all these properties, and the
    // getter and setter methods allow access and modification of these properties.
    public VenusPoint(String name, Point2D.Double p, Color sc, Color fc, int r, boolean sel) {
        this.name = name;
        this.p = p;
        this.sc = sc;
        this.fc = fc;
        this.r = r;
        this.sel = sel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point2D.Double getP() {
        return p;
    }

    public void setP(Point2D.Double p) {
        this.p = p;
    }

    public Color getSc() {
        return sc;
    }

    public void setSc(Color sc) {
        this.sc = sc;
    }

    public Color getFc() {
        return fc;
    }

    public void setFc(Color fc) {
        this.fc = fc;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public boolean isSel() {
        return sel;
    }

    public void setSel(boolean sel) {
        this.sel = sel;
    }
}//end VenusPoint
