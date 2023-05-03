package venus3d;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Utility {

    private int W, H;

    public Utility(int W, int H) {
        this.W = W;
        this.H = H;
    }

    //pattern
    public static Point2D.Double convertPoint(double ang, double x, double y, double cx, double cy) {
        return new Point2D.Double(
                Math.round(cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy)),
                Math.round(cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy))
        );
    }//end convert

    public static double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

    public static Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
        return new Line2D.Float(x1, y1, x2, y2);
    }//end makeLine

    public static Ellipse2D.Float drawEllipse(int x1, int y1, int size) {
        return new Ellipse2D.Float(x1 - (size / 2), y1 - (size / 2), size, size);
    }//end drawEllipse

    /*
            private List<PointObj> reducedSpliane(List<PointObj> list) {
        List<Point2D.Double> in = new ArrayList<>();
        List<Point2D.Double> out = new ArrayList<>();
        List<PointObj> tempLp = new ArrayList<>();
        list.forEach((p) -> {
            in.add(new Point2D.Double(p.getXy().x, p.getXy().y));
        });
        if (in.size() > 2) {
            RamerDouglasPeuckerAlgorithm.ramerDouglasPeucker(in, ramerDouglasPeuckerAlgorithm, out);
        }
        out.forEach((dp) -> {
            tempLp.add(new PointObj(new Point((int) dp.x, (int) dp.y), new Point(0, 0), new Point(0, 0), colorArray[color]));
        });
        return tempLp;
    }
    
        private void paintReducedSpliane(Graphics2D g2) {

        arrayOfArrays.stream().map((lp) -> {
            List<Point2D.Double> in = new ArrayList<>();
            List<Point2D.Double> out = new ArrayList<>();
            List<PointObj> tempLp = new ArrayList<>();
            lp.forEach((p) -> {
                in.add(new Point2D.Double(p.getXy().x, p.getXy().y));
            });
            if (in.size() > 2) {
                RamerDouglasPeuckerAlgorithm.ramerDouglasPeucker(in, 0.5, out);
            }
            out.forEach((dp) -> {
                tempLp.add(new PointObj(new Point((int) dp.x, (int) dp.y), new Point(0, 0), new Point(0, 0), colorArray[color]));
            });
            return tempLp;
        }).map((tempLp) -> {
            if (spline) {
                sp.drawSplines(tempLp);
            }
            return tempLp;
        }).map((tempLp) -> {
            if (spline) {
                paintSpliane(g2, tempLp);
            } else {
                paintLine(g2, tempLp);
            }

            return tempLp;
        }).forEachOrdered((tempLp) -> {
            if (dots) {
                paintSplianePoints(g2, tempLp);
            }
        });
    }//end paintReducedSpliane
     */
}

