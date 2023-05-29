package venus3d;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * The Utility class contains methods for converting points, creating lines, and drawing ellipses in
 * Java.
 */
public class Utility {
    
    // The `convertPoint` method takes in five parameters: `ang`, `x`, `y`, `cx`, and `cy`. It uses these
    // parameters to calculate and return a new `Point2D.Double` object.
    public static Point2D.Double convertPoint(double ang, double x, double y, double cx, double cy) {
        return new Point2D.Double(
                Math.round(cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy)),
                Math.round(cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy))
        );
    }//end convertPoint

    // The `convert` method takes in five parameters: `ang`, `x`, `y`, `cx`, and `cy`. It uses these
    // parameters to calculate and return a new `double` array with two elements. The first element is the
    // x-coordinate of the converted point, and the second element is the y-coordinate of the converted
    // point. The method uses trigonometric functions to perform the conversion based on the angle `ang`
    // and the center point `(cx, cy)`.
    public static double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

    // The `makeLine` method is creating a new `Line2D.Float` object with the specified coordinates
    // `(x1, y1)` and `(x2, y2)` and returning it. This method is useful for creating lines in Java
    // graphics.
    public static Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
        return new Line2D.Float(x1, y1, x2, y2);
    }//end makeLine

    // The `drawEllipse` method is creating a new `Ellipse2D.Float` object with the specified
    // coordinates `(x1 - (size / 2), y1 - (size / 2))` and size `size`, and returning it. This method
    // is useful for drawing ellipses in Java graphics.
    public static Ellipse2D.Float drawEllipse(int x1, int y1, int size) {
        return new Ellipse2D.Float(x1 - (size / 2), y1 - (size / 2), size, size);
    }//end drawEllipse

}//end Utility

