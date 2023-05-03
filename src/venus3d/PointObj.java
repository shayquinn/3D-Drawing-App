/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venus3d;

import java.awt.Color;
import java.awt.Point;

class PointObj {

    private Point3D p3;
    private Point xy, c1, c2;
    private int rxy, rc1, rc2;
    private Color color, cxy, cc1, cc2;
    private boolean mirror, selected;

    public PointObj(Point xy, Point c1, Point c2, int z, Color color) {
        this.p3 = new Point3D(xy.x, xy.y, z);
        this.xy = xy;
        this.c1 = c1;
        this.c2 = c2;
        this.rxy = 10;
        this.rc1 = 10;
        this.rc2 = 10;
        this.cxy = Color.BLUE;
        this.cc1 = Color.RED;
        this.cc2 = Color.RED;
        this.mirror = true;
        this.selected = false;
    }//end PointObj#

    public PointObj(Point xy, Point c1, Point c2, Color color) {
        this.p3 = new Point3D(xy.x, xy.y, 0);
        this.xy = xy;
        this.c1 = c1;
        this.c2 = c2;
        this.rxy = 10;
        this.rc1 = 10;
        this.rc2 = 10;
        this.color = color;
        this.cxy = Color.BLUE;
        this.cc1 = Color.RED;
        this.cc2 = Color.RED;
        this.mirror = true;
        this.selected = false;
    }//end PointObj

    @Override
    public String toString() {
        return "PointObj{" + "p3=" + p3 + ", xy=" + xy + ", c1=" + c1 + ", c2=" + c2 + ", rxy=" + rxy + ", rc1=" + rc1 + ", rc2=" + rc2 + ", cxy=" + cxy + ", cc1=" + cc1 + ", cc2=" + cc2 + ", mirror=" + mirror + ", selected=" + selected + '}';
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point3D getP3() {
        return p3;
    }

    public void setP3(Point3D p3) {
        this.p3 = p3;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getCxy() {
        return cxy;
    }

    public void setCxy(Color cxy) {
        this.cxy = cxy;
    }

    public Color getCc1() {
        return cc1;
    }

    public void setCc1(Color cc1) {
        this.cc1 = cc1;
    }

    public Color getCc2() {
        return cc2;
    }

    public void setCc2(Color cc2) {
        this.cc2 = cc2;
    }

    public int getRxy() {
        return rxy;
    }

    public void setRxy(int rxy) {
        this.rxy = rxy;
    }

    public int getRc1() {
        return rc1;
    }

    public void setRc1(int rc1) {
        this.rc1 = rc1;
    }

    public int getRc2() {
        return rc2;
    }

    public void setRc2(int rc2) {
        this.rc2 = rc2;
    }

    public Point getXy() {
        return xy;
    }

    public void setXy(Point xy) {
        this.xy = xy;
    }

    public Point getC1() {
        return c1;
    }

    public void setC1(Point c1) {
        this.c1 = c1;
    }

    public Point getC2() {
        return c2;
    }

    public void setC2(Point c2) {
        this.c2 = c2;
    }

}//end PointObj
