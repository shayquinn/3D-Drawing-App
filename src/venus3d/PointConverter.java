package venus3d;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class PointConverter {

    private static double scale = 1;
    private static int perspective = 1400;
    static double zz;
    
    public static Point convertPoint(Point3D p) {
        double x3d = p.getY() * scale;
        double y3d = p.getZ() * scale;
        double depth = p.getX() * scale;
        double[] newVal = scale(x3d, y3d, depth);
        int x2d = (int) (Venus3D.getScreenW() / 2 + newVal[0]);
        int y2d = (int) (Venus3D.getScreenH() / 2 - newVal[1]);
        Point point2D = new Point(x2d, y2d);
        return point2D;
    }
    
    public static Point convertPoint(PointObj p) {
        double x3d = p.getP3().getY() * scale;
        double y3d = p.getP3().getZ() * scale;
        double depth = p.getP3().getX() * scale;
        double[] newVal = scale(x3d, y3d, depth);
        int x2d = (int) (Venus3D.getScreenW() / 2 + newVal[0]);
        int y2d = (int) (Venus3D.getScreenH() / 2 - newVal[1]);
        Point point2D = new Point(x2d, y2d);
        return point2D;
    }

    public static List<PointObj> convertPoints(List<PointObj> pa) {
        for (PointObj po : pa) {
            double x3d = po.getP3().getY() * scale;
            double y3d = po.getP3().getZ() * scale;
            double depth = po.getP3().getX() * scale;
            double[] newVal = scale(x3d, y3d, depth);
            int x2d = (int) (Venus3D.getScreenW() / 2 + newVal[0]);
            int y2d = (int) (Venus3D.getScreenH() / 2 - newVal[1]);
            po.setXy( new Point(x2d, y2d));
        }
        return pa;
    }

    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt(x3d * x3d + y3d * y3d);//distance from oragin
        double theta = Math.atan2(y3d, x3d);
        double depth2 = 15 - depth;
        double localScale = Math.abs(perspective / (depth2 + perspective));

        dist *= localScale;
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(theta);
        newVal[1] = dist * Math.sin(theta);
        return newVal;
    }

    public static void rotateAxisX(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getY() * p.getP3().getY() + p.getP3().getZ() * p.getP3().getZ());
        double theta = Math.atan2(p.getP3().getZ(), p.getP3().getY());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D(p.getP3().getX(), (radius * Math.cos(theta)), (radius * Math.sin(theta))));
    }

    public static void rotateAxisY(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getX() * p.getP3().getX() + p.getP3().getZ() * p.getP3().getZ());
        double theta = Math.atan2(p.getP3().getX(), p.getP3().getZ());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D((radius * Math.sin(theta)), p.getP3().getY(), (radius * Math.cos(theta))));
    }

    public static void rotateAxisZ(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getY() * p.getP3().getY() + p.getP3().getX() * p.getP3().getX());
        double theta = Math.atan2(p.getP3().getY(), p.getP3().getX());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D((radius * Math.cos(theta)), (radius * Math.sin(theta)), p.getP3().getZ()));
    }
    

    
   

}

