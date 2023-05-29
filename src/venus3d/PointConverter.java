package venus3d;

import java.awt.Point;
import java.util.List;


/**
 * The PointConverter class contains methods for converting 3D points to 2D points, rotating points
 * along different axes, and scaling points based on perspective.
 */
public class PointConverter {

    private static double scale = 1;
    private static int perspective = 1400;
    static double zz;
    
/**
 * The function converts a 3D point to a 2D point with scaling and returns the 2D point.
 * 
 * @param p A Point3D object representing a point in 3D space.
 * @return A Point object representing the 2D coordinates of the converted Point3D object.
 */
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
    
/**
 * The function converts a 3D point object to a 2D point object with scaling and positioning.
 * 
 * @param p A PointObj object that contains a 3D point with x, y, and z coordinates.
 * @return A 2D point object.
 */
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

/**
 * This function converts a list of 3D points to 2D points and scales them based on a given scale
 * factor.
 * 
 * @param pa A List of PointObj objects that need to be converted from 3D coordinates to 2D
 * coordinates.
 * @return The method is returning a List of PointObj objects after converting their 3D coordinates to
 * 2D coordinates and setting their xy values.
 */
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

/**
 * The function scales a 3D point based on its distance from the origin and its depth, and returns the
 * new scaled values.
 * 
 * @param x3d The x-coordinate of a point in 3D space.
 * @param y3d The y-coordinate of a point in a 3D space.
 * @param depth The depth parameter represents the distance of the object from the viewer's
 * perspective.
 * @return An array of two doubles, which represent the scaled x and y coordinates of a 3D point in a
 * 2D projection.
 */
/**
 * The function scales a 3D point based on its distance from the origin and its depth, and returns the
 * new scaled values.
 * 
 * @param x3d The x-coordinate of a point in 3D space.
 * @param y3d The y-coordinate of a point in a 3D space.
 * @param depth The depth parameter represents the distance of the object from the viewer's
 * perspective.
 * @return An array of two doubles, which represent the scaled x and y coordinates of a 3D point in a
 * 2D projection.
 */
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

/**
 * The function rotates a 3D point around the X-axis by a specified angle in either clockwise or
 * counterclockwise direction.
 * 
 * @param p A PointObj object representing a point in 3D space.
 * @param CW A boolean value indicating whether the rotation should be clockwise (true) or
 * counterclockwise (false).
 * @param degrees The amount of rotation in degrees that the point will undergo around the X-axis.
 */
    public static void rotateAxisX(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getY() * p.getP3().getY() + p.getP3().getZ() * p.getP3().getZ());
        double theta = Math.atan2(p.getP3().getZ(), p.getP3().getY());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D(p.getP3().getX(), (radius * Math.cos(theta)), (radius * Math.sin(theta))));
    }

/**
 * The function rotates a 3D point around the Y-axis by a specified angle in either clockwise or
 * counterclockwise direction.
 * 
 * @param p The PointObj object that represents the point to be rotated.
 * @param CW A boolean value indicating whether the rotation should be clockwise (true) or
 * counterclockwise (false).
 * @param degrees The amount of rotation in degrees.
 */
    public static void rotateAxisY(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getX() * p.getP3().getX() + p.getP3().getZ() * p.getP3().getZ());
        double theta = Math.atan2(p.getP3().getX(), p.getP3().getZ());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D((radius * Math.sin(theta)), p.getP3().getY(), (radius * Math.cos(theta))));
    }

/**
 * This function rotates a point around the z-axis by a specified angle in either clockwise or
 * counterclockwise direction.
 * 
 * @param p The object of type PointObj that represents the point to be rotated.
 * @param CW A boolean value indicating whether the rotation should be clockwise (true) or
 * counterclockwise (false).
 * @param degrees The amount of rotation in degrees.
 */
    public static void rotateAxisZ(PointObj p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.getP3().getY() * p.getP3().getY() + p.getP3().getX() * p.getP3().getX());
        double theta = Math.atan2(p.getP3().getY(), p.getP3().getX());
        theta += 2 * Math.PI / 360 * degrees * (CW ? -1 : 1);
        p.setP3(new Point3D((radius * Math.cos(theta)), (radius * Math.sin(theta)), p.getP3().getZ()));
    }

}//end PointConverter

