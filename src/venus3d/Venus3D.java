package venus3d;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
//import sun.awt.image.codec.JPEGImageEncoderImpl;

public class Venus3D extends JPanel implements ActionListener {

    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenW = (int) screenSize.getWidth();
    private static int screenH = (int) screenSize.getHeight();

    Timer timer = new Timer(40, this);

    AtomicInteger counter0 = new AtomicInteger(0);

    public static int getScreenW() {
        return screenW;
    }

    public static int getScreenH() {
        return screenH;
    }

    private double rotX = 0, rotY = 0, rotZ = 0;
    private final Spline sp;

    private final PaintSurface paintSurface;
    //list varables
    private List<ArrayList<PointObj>> arrayOfArrays = new ArrayList<>();
    private BufferedImage canvas;
    JLabel canvasLabel;
    Graphics2D g2d;

    //boolean varables
    private boolean mouseDown;
    private Point mouse;
    private final boolean grid = false;
    private boolean spline = false;
    private boolean dots = false;
    private boolean move = false;

    private boolean xx = false, yy = false, zz = true;

    ArrayList<Point2D.Double> Earth = new ArrayList<>();
    ArrayList<Point2D.Double> Venus = new ArrayList<>();
    ArrayList<Point2D.Double> VMoon = new ArrayList<>();

    // Point2D.Double ratio = new Point2D.Double(0.5, 0.75);
    Point2D.Double ratio = new Point2D.Double(3.25, 2);
    private Point diff = new Point(0, 0);
    //int varables

    List<PointObj> lp;

    List<Polygon3D> polygonList;

    public static int color = 0;
    public static Color[] colorArray = {
        new Color(255, 0, 0, 50),
        new Color(255, 255, 0, 50),
        new Color(0, 255, 0, 50),
        new Color(0, 255, 255, 50),
        new Color(0, 0, 255, 50),
        new Color(255, 0, 255, 50)
    };

    private List<PointObj> axis3d;
    private boolean AxisBull = true;

    private List<PointObj> space3d;
    private boolean SpaceBull = true;

    private boolean stopStart = false;

    private static double ramerDouglasPeuckerAlgorithm = 5;
    private static double splaineTension = 0.5;

    public static double getSplaineTension() {
        return splaineTension;
    }

    Point cp = new Point(screenW / 2, screenH / 2);
    VenusPoint[] vpa = {
        new VenusPoint("E", new Point2D.Double(cp.x, cp.y - 20), Color.GREEN, Color.white, 60, false),
        new VenusPoint("S", new Point2D.Double(cp.x + 200, cp.y - 20), Color.ORANGE, Color.white, 40, false),
        new VenusPoint("V", new Point2D.Double(cp.x + 350, cp.y - 20), Color.YELLOW, Color.white, 20, false),
        new VenusPoint("Vm", new Point2D.Double(cp.x + 400, cp.y - 20), Color.RED, Color.white, 10, false)
    };

// The below code is a constructor for a class called Venus3D. It initializes various objects and
// variables, sets the size of the window, creates arrays, creates a canvas for drawing, sets up the 3D
// space and axis, and starts a timer.
    public Venus3D() {
        new Utility();
        sp = new Spline();

        this.setSize(screenW, screenH);
        paintSurface = new PaintSurface();
        this.add(paintSurface, BorderLayout.CENTER);
        this.setVisible(true);

        createArrays(vpa[0], vpa[1], vpa[2], vpa[3], 0, ratio);

        canvas = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);
        canvasLabel = new JLabel(new ImageIcon(canvas));
        g2d = canvas.createGraphics();

        setSpace();
        setAxis3d();
        timer.start();

    }//end Venus3D

// The below code is defining a method called "setSpace" which initializes or clears an ArrayList
// called "space3d" and adds 8 PointObj objects to it. These PointObj objects represent the corners of
// a 2D space in 3D coordinates, with each PointObj having a position, velocity, acceleration,
// rotation, and color. The space is defined as a rectangle with width "screenW" and height "screenH",
// with the corners being (-screenW/2, -screenH/2), (screenW/2, -screenH/2), (screenW
    private void setSpace() {
        if (space3d == null) {
            space3d = new ArrayList<>();
        } else {
            space3d.clear();
        }

        space3d.add(new PointObj(new Point(-(screenW / 2), -(screenH / 2)), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(screenW / 2, -(screenH / 2)), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(screenW / 2, screenH / 2), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(-screenW / 2, screenH / 2), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));

        space3d.add(new PointObj(new Point(-(screenW / 2), -(screenH / 2)), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(screenW / 2, -(screenH / 2)), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(screenW / 2, screenH / 2), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        space3d.add(new PointObj(new Point(-screenW / 2, screenH / 2), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));

    }//end setSpace

// The below code is defining a method called "setAxis3d" which creates or clears an ArrayList called
// "axis3d" and adds six PointObj objects to it. These PointObj objects represent the x, y, and z axes
// of a 3D coordinate system, with two points for each axis representing the start and end points of
// the axis line. The method also sets the color of the axes using an array called "colorArray".
    private void setAxis3d() {
        if (axis3d == null) {
            axis3d = new ArrayList<>();
        } else {
            axis3d.clear();
        }
        axis3d.add(new PointObj(new Point(300, 0), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        axis3d.add(new PointObj(new Point(-300, 0), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));

        axis3d.add(new PointObj(new Point(0, 300), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));
        axis3d.add(new PointObj(new Point(0, -300), new Point(0, 0), new Point(0, 0), 0, colorArray[color]));

        axis3d.add(new PointObj(new Point(0, 0), new Point(0, 0), new Point(0, 0), 300, colorArray[color]));
        axis3d.add(new PointObj(new Point(0, 0), new Point(0, 0), new Point(0, 0), -300, colorArray[color]));
    }//end setAxis3d

    public void keyPressed(KeyEvent e) {
        repaint();
    }

// The code is an implementation of the actionPerformed method in a Java class. It checks if a timer
// event has occurred and if the mouse is currently being pressed down. If so, it adds a new PointObj
// to an ArrayList of ArrayLists called arrayOfArrays. It then iterates through each ArrayList in
// arrayOfArrays and updates the x and y coordinates of each PointObj using a PointConverter class. If
// a boolean variable stopStart is true, it rotates each ArrayList around the z-axis using a rotate
// method. It then creates a list of Polygon3D objects using the craeatePolyListPent method
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {
            if (mouseDown) {
                if (arrayOfArrays.size() > 0) {
                    arrayOfArrays.get(
                            arrayOfArrays.size() - 1).add(
                            new PointObj(new Point(90, mouse.x - (screenW / 2)),
                                    new Point(0, 0),
                                    new Point(0, 0),
                                    screenH - mouse.y - (screenH / 2),
                                    colorArray[color]
                            )
                    );
                }
            }

            for (ArrayList<PointObj> apo : arrayOfArrays) {
                for (int i = 0; i < apo.size(); i++) {
                    apo.get(i).setXy(PointConverter.convertPoint(apo.get(i)));
                }
                if (stopStart) {

                    rotate(apo, true, 0, 0, 1);
                }
            }

            // polygonList = craeatePolyList(arrayOfArrays, 10);
            polygonList = craeatePolyListPent(arrayOfArrays, 70, 12, colorArray[4], true, rotX, rotY, rotZ);
            for (Polygon3D p3 : polygonList) {
                p3.setP2d1(PointConverter.convertPoint(p3.getP1()));
                p3.setP2d2(PointConverter.convertPoint(p3.getP2()));
                p3.setP2d3(PointConverter.convertPoint(p3.getP3()));
            }

            /*
            PointConverter.convertPoints(space3d);
            if (stopStart) {
                rotate(axis3d, true, 0, 0, 1);
            }

            PointConverter.convertPoints(axis3d);
            if (stopStart) {
                rotate(axis3d, true, 0, 0, 1);
            }
             */
            /*
            for (int i = 0; i < lp.size(); i++) {
                lp.get(i).setXy(PointConverter.convertPoint(lp.get(i)));
            }
            rotate(lp, true, 1, 1, 1);
             */
            repaint();

        }
    }//end actionPerformed

// The below code is a Java method that takes in a list of lists of PointObj objects representing
// polygons, along with other parameters such as line width, color, and rotation angles. It then
// creates a list of Polygon3D objects by iterating through each polygon in the input list and creating
// a series of triangles to represent the polygon in 3D space. The resulting list of Polygon3D objects
// is returned.
    private List<Polygon3D> craeatePolyListPent(
            List<ArrayList<PointObj>> pol,
            int lineW,
            int num,
            Color color,
            boolean frame,
            double rx,
            double ry,
            double rz
    ) {
        List<Polygon3D> pl = new ArrayList<>();
        for (int i = 0; i < pol.size(); i++) {
            for (int j = 0; j < pol.get(i).size() - 1; j++) {
                Point3D[] pa1 = pol.get(i).get(j).getP3().createPoints(num, lineW, rx, ry, rz);
                Point3D[] pa2 = pol.get(i).get(j + 1).getP3().createPoints(num, lineW, rx, ry, rz);
                for (int l = 0; l < num; l++) {
                    if (l == num - 1) {
                        pl.add(
                                new Polygon3D(
                                        pa1[l],
                                        pa2[l],
                                        pa2[0],
                                        color,
                                        frame
                                )
                        );
                        pl.add(
                                new Polygon3D(
                                        pa2[0],
                                        pa1[0],
                                        pa1[l],
                                        color,
                                        frame
                                )
                        );
                    } else {
                        pl.add(
                                new Polygon3D(
                                        pa1[l],
                                        pa2[l],
                                        pa2[l + 1],
                                        color,
                                        frame
                                )
                        );
                        pl.add(
                                new Polygon3D(
                                        pa2[l + 1],
                                        pa1[l + 1],
                                        pa1[l],
                                        color,
                                        frame
                                )
                        );
                    }
                }
            }
        }
        return pl;
    }// end craeatePolyList

   /**
    * The PaintSurface class is a JComponent that handles mouse events and painting of shapes on a 3D
    * plane.
    */
    private class PaintSurface extends JComponent {

        public PaintSurface() {
            this.setPreferredSize(new Dimension(screenW, screenH));

            this.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int steps = e.getWheelRotation();
                    if (steps > 0) {
                        for (ArrayList<PointObj> apo : arrayOfArrays) {
                            for (int i = 0; i < apo.size(); i++) {
                                apo.get(i).setXy(PointConverter.convertPoint(apo.get(i)));
                            }
                            if (xx) {
                                rotate(apo, true, 5, 0, 0);
                                rotate(axis3d, true, 5, 0, 0);
                            }
                            if (yy) {
                                rotate(apo, true, 0, 5, 0);
                                rotate(axis3d, true, 0, 5, 0);
                            }
                            if (zz) {
                                rotate(apo, true, 0, 0, 5);
                                rotate(axis3d, true, 0, 0, 5);
                            }

                        }
                        repaint();
                    } else {
                        System.out.println(steps);
                        for (ArrayList<PointObj> apo : arrayOfArrays) {
                            for (int i = 0; i < apo.size(); i++) {
                                apo.get(i).setXy(PointConverter.convertPoint(apo.get(i)));
                            }
                            if (xx) {
                                rotate(apo, true, -5, 0, 0);
                                rotate(axis3d, true, -5, 0, 0);
                            }
                            if (yy) {
                                rotate(apo, true, 0, -5, 0);
                                rotate(axis3d, true, 0, -5, 0);
                            }
                            if (zz) {
                                rotate(apo, true, 0, 0, -5);
                                rotate(axis3d, true, 0, 0, -5);
                            }

                        }
                        repaint();
                    }

                }//end mouseWheelMoved
            });
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }//end mouseClicked

                @Override
                public void mouseEntered(MouseEvent e) {
                }//end mouseEntered

                @Override
                public void mouseExited(MouseEvent e) {
                }//end mouseExited

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        mouseDown = true;
                        mouse = new Point(e.getX(), e.getY());
                        arrayOfArrays.add(new ArrayList<>());
                        arrayOfArrays.get(
                                arrayOfArrays.size() - 1).add(
                                new PointObj(new Point(90, mouse.x - (screenW / 2)),
                                        new Point(0, 0),
                                        new Point(0, 0),
                                        screenH - mouse.y - (screenH / 2),
                                        colorArray[color]
                                )
                        );
                        repaint();
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                    }//System.out.println("Middle Click!");
                    if (e.getButton() == MouseEvent.BUTTON3) {

                        context(e);
                    }
                }//end mouseExited

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        mouseDown = false;

                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                    }//System.out.println("Middle Click!");
                    if (e.getButton() == MouseEvent.BUTTON3) {
                    }
                    color++;
                    if (color == colorArray.length - 1) {
                        color = 0;
                    }
                }//end mouseReleased    
            });//end addMouseListener 

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    mouse = new Point(e.getX(), e.getY());
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (move) {
                            //diff = new Point(staticDiff.x + (mouse.x - snapMouse.x), staticDiff.y + (mouse.y - snapMouse.y));
                        } else {
                            if (arrayOfArrays.size() > 0) {
                                arrayOfArrays.get(
                                        arrayOfArrays.size() - 1).add(
                                        new PointObj(new Point(90, mouse.x - (screenW / 2)),
                                                new Point(0, 0),
                                                new Point(0, 0),
                                                screenH - mouse.y - (screenH / 2),
                                                colorArray[color]
                                        )
                                );
                            }
                        }
                    }

                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    mouse = new Point(e.getX(), e.getY());
                }
            });//end addMouseMotionListener                
        }//end PaintSurface constructer

    }//end PaintSurface

// The below code is defining a method called "context" that creates a JPopupMenu with various
// JMenuItem options. It sets the background color of certain options based on certain conditions. It
// also adds ActionListeners to each JMenuItem option that perform certain actions when clicked, such
// as toggling certain features or saving an image. Finally, it displays the JPopupMenu at the location
// of a MouseEvent.
    public void context(MouseEvent e) {
        JPopupMenu jp = new JPopupMenu();
        JMenuItem ss = new JMenuItem("Stop-Strat");
        JMenuItem o2 = new JMenuItem("Toggle dots");
        JMenuItem o3 = new JMenuItem("Toggle spaline");
        JMenuItem snap = new JMenuItem("Take picture");
        JMenuItem ta = new JMenuItem("Toggle Axis");
        JMenuItem x = new JMenuItem("X Axis");
        JMenuItem y = new JMenuItem("Y Axis");
        JMenuItem z = new JMenuItem("Z Axis");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem o5 = new JMenuItem("Clear");
        JMenuItem o6 = new JMenuItem("Exit");

        if (stopStart) {
            ss.setBackground(Color.LIGHT_GRAY);
        }

        if (AxisBull) {
            ta.setBackground(Color.LIGHT_GRAY);
        }
        if (spline) {
            o3.setBackground(Color.LIGHT_GRAY);
        }
        if (xx) {
            x.setBackground(Color.LIGHT_GRAY);
        }
        if (yy) {
            y.setBackground(Color.LIGHT_GRAY);
        }
        if (zz) {
            z.setBackground(Color.LIGHT_GRAY);
        }

        if (dots) {
            o2.setBackground(Color.lightGray);
        }

        ss.setActionCommand("Stop-Strat");
        o2.setActionCommand("Toggle dots");
        o3.setActionCommand("Toggle spaline");
        snap.setActionCommand("Take picture");
        ta.setActionCommand("Toggle Axis");
        x.setActionCommand("X Axis");
        y.setActionCommand("Y Axis");
        z.setActionCommand("Z Axis");
        save.setActionCommand("Save");
        o5.setActionCommand("Clear");
        o6.setActionCommand("Exit");

        ss.addActionListener(this);
        o2.addActionListener(this);
        o3.addActionListener(this);
        snap.addActionListener(this);
        ta.addActionListener(this);
        x.addActionListener(this);
        y.addActionListener(this);
        z.addActionListener(this);
        save.addActionListener(this);
        o5.addActionListener(this);
        o6.addActionListener(this);

        jp.add(ss);
        jp.add(o2);
        jp.add(o3);
        jp.add(snap);
        jp.add(ta);
        jp.add(x);
        jp.add(y);
        jp.add(z);
        jp.add(save);
        jp.add(o5);
        jp.add(o6);

        jp.show(e.getComponent(), e.getX() + 10, e.getY() + 10);

        ss.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle move");
            if (stopStart) {
                stopStart = false;
            } else {
                stopStart = true;
            }
            repaint();

        });

        o2.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle dots");
            if (dots) {
                dots = false;
            } else {
                dots = true;
            }
            repaint();
        });
        o3.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle spaline");
            if (spline) {
                spline = false;
            } else {
                spline = true;
            }
        });
        snap.addActionListener((ActionEvent e1) -> {
            // BufferedImage awtImage = new BufferedImage(paintSurface.getWidth(), paintSurface.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = canvas.getGraphics();
            paintSurface.printAll(g);
            JFileChooser fc = new JFileChooser();

            fc.setDialogTitle("Specify a file to save");
            int userSelection = fc.showSaveDialog(Venus3D.this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {

                try {
                    ImageIO.write(canvas, "png", new File(
                            fc.getSelectedFile().getAbsolutePath()
                            + fc.getSelectedFile().getName()));
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }

            System.out.println("Take picture");
            // prints the name of the system property
            System.out.println(System.getProperty("user.dir"));

            // prints the name of the Operating System
            System.out.println(System.getProperty("os.name"));

            // prints Java Runtime Version
            System.out.println(System.getProperty("java.runtime.version"));
        });

        ta.addActionListener((ActionEvent e1) -> {
            if (AxisBull) {
                AxisBull = false;
            } else {
                AxisBull = true;
            }
            repaint();
            System.out.println("Toggle Axis");
        });

        x.addActionListener((ActionEvent e1) -> {
            if (!xx) {
                xx = true;
                yy = false;
                zz = false;
            }
            System.out.println("Toggle X");
        });
        y.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle Y");
            if (!yy) {
                yy = true;
                xx = false;
                zz = false;
            }
        });
        z.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle Z");
            if (!zz) {
                zz = true;
                yy = false;
                xx = false;
            }
        });

        save.addActionListener((ActionEvent e1) -> {
            System.out.println("Toggle spaline");
        });

        o5.addActionListener((ActionEvent e1) -> {
            System.out.println("Clear");
            arrayOfArrays.clear();
            setAxis3d();
            repaint();
        });
        o6.addActionListener((ActionEvent e1) -> {
            System.out.println("Exit");
            System.exit(1);
        });
    }//end context

// The below code is overriding the paint method of a Java graphics object. It sets the background
// color to black and enables anti-aliasing. It then fills the entire graphics object with a light gray
// color. It calls the pintRotation and Spliane methods to draw some graphics. It also has some
// conditional statements that are currently commented out, which may be used to draw additional
// graphics depending on the values of certain variables. Finally, it sets the composite of the
// graphics object to be fully opaque.
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g2d.setBackground(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2d.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        pintRotation(g2);

        if (SpaceBull) {

        }

        // space(g2);
        if (AxisBull) {
            //  axis(g2);
        }

        if (grid) {
            paintBackground(g2);
        }
        Spliane(g2);
        //paintReducedSpliane(g2);
        // painPattern(g2);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        //order();
    }//end paint

// The below code is defining a private method called "pintRotation" that takes a Graphics2D object as
// a parameter. Within the method, it sets the stroke to a BasicStroke object with a width of 2, sets
// the paint color to black, and draws a string that displays the values of rotX, rotY, and rotZ
// variables at the position (70, 20) on the graphics object.
    private void pintRotation(Graphics2D g2) {
        BasicStroke lw = new BasicStroke(2);
        g2.setStroke(lw);
        g2.setPaint(Color.BLACK);
        g2.drawString("X:" + rotX + ", Y:" + rotY + ", Z:" + rotZ + ",", 70, 20);
    }//end pintRotation

// The below code is defining a method called `paintBackground` that takes a `Graphics2D` object as a
// parameter. This method is used to paint a grid-like background on a graphical user interface. It
// sets the paint color to light gray, creates a basic stroke with a width of 1, and creates a general
// path with a non-zero winding rule. It then uses two for loops to draw vertical and horizontal lines
// at intervals of 100 pixels. Finally, it sets the stroke and paint color to light gray and draws the
// path.
    private void paintBackground(Graphics2D g2) {
        g2.setPaint(Color.LIGHT_GRAY);
        BasicStroke lw = new BasicStroke(1);
        GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        for (int i = 0; i < getSize().width; i += 100) {
            p.moveTo(i, 0);
            p.lineTo(i, screenH);

        }
        for (int i = 0; i < getSize().height; i += 100) {
            p.moveTo(0, i);
            p.lineTo(screenW, i);
        }
        g2.setStroke(lw);
        g2.setPaint(Color.LIGHT_GRAY);
        g2.draw(p);

    }//end paintBackground

// The below code is a method in Java that takes a Graphics2D object and a list of Polygon3D objects as
// input parameters. It converts the 3D polygons to 2D polygons and paints them on the graphics object.
// It sets the stroke width to 1 and the color of the polygon based on the color of the Polygon3D
// object. If the polygon is a frame, it draws the polygon outline, otherwise it fills the polygon. It
// also draws a line from the centroid of the polygon to the center of the screen. If the "dots" flag
// is set, it also
    private void paintLinePoly(Graphics2D g2, List<Polygon3D> pl) {
        BasicStroke lw = new BasicStroke(1);
        g2.setStroke(lw);
        for (Polygon3D pli : pl) {
            Polygon poly = new Polygon();
            poly.addPoint(pli.getP2d1().x, pli.getP2d1().y);
            poly.addPoint(pli.getP2d2().x, pli.getP2d2().y);
            poly.addPoint(pli.getP2d3().x, pli.getP2d3().y);
            g2.setColor(pli.getC());

            if (pli.isFrame()) {
                g2.drawPolygon(poly);
            } else {
                g2.fillPolygon(poly);
            }
            Point centroid = PointConverter.convertPoint(pli.centroid());

            GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
            p.moveTo(centroid.x, centroid.y);
            p.lineTo(screenW / 2, screenH / 2);

            //Shape s1 = Utility.drawEllipse(centroid.x, centroid.y, 3);
            Color sc = Color.RED;
            g2.setPaint(sc);
            // g2.draw(p);
            //g2.fill(s1);
            if (dots) {
                paintPolyPoints(g2, pli.getP2d1(), pli.getP2d2(), pli.getP2d3());
            }
        }
    }//end paintLine

// The below code is defining a method called "paintPolyPoints" that takes in a Graphics2D object and
// three Point objects as parameters. Inside the method, it creates three Shape objects (ellipses)
// using the Utility.drawEllipse method and sets their positions based on the Point objects passed in.
// It then sets the stroke and paint color for the Graphics2D object and draws the three ellipses on
// the screen using the g2d.draw method. The method is likely used to visually represent points in a
// polygon.
    private void paintPolyPoints(Graphics2D g2, Point pl, Point p2, Point p3) {
        Shape s1 = Utility.drawEllipse(pl.x, pl.y, 3);
        Shape s2 = Utility.drawEllipse(p2.x, p2.y, 3);
        Shape s3 = Utility.drawEllipse(p3.x, p3.y, 3);

        BasicStroke lw = new BasicStroke(3);
        Color sc = Color.RED;

        g2.setStroke(lw);
        g2.setPaint(sc);
        g2.draw(s1);
        g2.draw(s2);
        g2.draw(s3);

        g2d.setStroke(lw);
        g2d.setPaint(sc);
        g2d.draw(s1);
        g2d.draw(s2);
        g2d.draw(s3);

    }//end paintPoints

// The below code is defining a private method called "Spliane" that takes a Graphics2D object as a
// parameter. Within the method, there is a conditional statement that checks if a boolean variable
// "spline" is true. If it is true, then the method calls another method called "paintSpliane" and
// passes in the Graphics2D object and the result of calling a method called "drawSplines" on an object
// called "sp" with a parameter of an array of arrays. If "spline" is false, then the method calls
// another method called "paintLinePoly"
    private void Spliane(Graphics2D g2) {
        if (spline) {
            arrayOfArrays.forEach((lp) -> {
                paintSpliane(g2, sp.drawSplines(lp));

            });
        } else {
            arrayOfArrays.forEach((lp) -> {
                // paintLine(g2, lp);
                paintLinePoly(g2, polygonList);

            });
        }
    }//end Spliane

// The below code is a method in Java that takes in a Graphics2D object and a List of PointObj objects
// as parameters. It iterates through the list of PointObj objects and draws a red ellipse with a black
// outline at each point on the Graphics2D object. The ellipse is drawn using the drawEllipse method
// from the Utility class, and the position of the ellipse is adjusted by the diff.x and diff.y values.
// The method uses a BasicStroke object with a width of 3 to draw the ellipse.
    private void paintSplianePoints(Graphics2D g2, List<PointObj> lp) {
        Shape r;
        BasicStroke lw = new BasicStroke(3);
        Color sc = Color.RED;
        for (PointObj p : lp) {
            r = Utility.drawEllipse(p.getXy().x + diff.x, p.getXy().y + diff.y, 3);
            g2.setStroke(lw);
            g2.setPaint(sc);
            g2.draw(r);

            g2d.setStroke(lw);
            g2d.setPaint(sc);
            g2d.draw(r);
        }
    }//end paintSplianePoints

// The below code is a Java method that takes in a Graphics2D object and a List of PointObj objects as
// parameters. It is used to draw a spline curve based on the points in the List. The method first sets
// the stroke width and color, and then checks the size of the List to determine how to draw the curve.
// If the List has only one point, it draws a small ellipse at that point. If the List has two points,
// it draws a straight line between them. If the List has three or more points, it draws a spline curve
// using the quadratic or cubic Bezier
    private void paintSpliane(Graphics2D g2, List<PointObj> lp) {
        int c = 2;
        int sw = 10;
        Shape r;
        BasicStroke lw = new BasicStroke(sw);

        // System.out.println(count + "    " + arrayOfArrays.size());
        g2.setColor(Color.BLACK);

        if (!(lp.isEmpty())) {
            if (lp.size() == 1) {
                r = Utility.drawEllipse(lp.get(0).getXy().x + diff.x, lp.get(0).getXy().y + diff.y, 2);
                g2.setStroke(lw);
                g2.setPaint(colorArray[c]);
                g2.draw(r);

                g2d.setStroke(lw);
                g2d.setPaint(colorArray[c]);
                g2d.draw(r);
            } else if (lp.size() == 2) {
                GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                p.moveTo(lp.get(0).getXy().x + diff.x, lp.get(0).getXy().y + diff.y);
                p.lineTo(lp.get(1).getXy().x + diff.x, lp.get(1).getXy().y + diff.y);
                g2.setStroke(lw);
                g2.setPaint(colorArray[c]);
                g2.draw(p);

                g2d.setStroke(lw);
                g2d.setPaint(colorArray[c]);
                g2d.draw(p);
            } else if (lp.size() == 3) {
                GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                p.moveTo(lp.get(0).getXy().x + diff.x, lp.get(0).getXy().y + diff.y);
                p.quadTo(lp.get(1).getC1().x + diff.x, lp.get(1).getC1().y + diff.y, lp.get(1).getXy().x + diff.x, lp.get(1).getXy().y + diff.y);
                p.quadTo(lp.get(1).getC2().x + diff.x, lp.get(1).getC2().y + diff.y, lp.get(2).getXy().x + diff.x, lp.get(2).getXy().y + diff.y);
                g2.setStroke(lw);
                g2.setPaint(colorArray[c]);
                g2.draw(p);

                g2d.setStroke(lw);
                g2d.setPaint(colorArray[c]);
                g2d.draw(p);
            } else if (lp.size() > 3) {
                GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                p.moveTo(lp.get(0).getXy().x + diff.x, lp.get(0).getXy().y + diff.y);
                p.quadTo(lp.get(1).getC1().x + diff.x, lp.get(1).getC1().y + diff.y, lp.get(1).getXy().x + diff.x, lp.get(1).getXy().y + diff.y);
                for (int i = 2; i < lp.size() - 1; i += 1) {
                    p.curveTo(
                            lp.get(i - 1).getC2().x + diff.x, lp.get(i - 1).getC2().y + diff.y,
                            lp.get(i).getC1().x + diff.x, lp.get(i).getC1().y + diff.y,
                            lp.get(i).getXy().x + diff.x, lp.get(i).getXy().y + diff.y);
                }
                p.quadTo(
                        lp.get(lp.size() - 2).getC2().x + diff.x, lp.get(lp.size() - 2).getC2().y + diff.y,
                        lp.get(lp.size() - 1).getXy().x + diff.x, lp.get(lp.size() - 1).getXy().y + diff.y
                );
                g2.setStroke(lw);
                g2.setPaint(colorArray[c]);
                g2.draw(p);

                g2d.setStroke(lw);
                g2d.setPaint(colorArray[c]);
                g2d.draw(p);
            }
        }
        if (dots) {
            paintSplianePoints(g2, lp);
        }
    }//end paintSpliane

// The below code is a Java method that takes a list of 2D points as input and returns a list of
// PointObj objects. It first creates two empty lists, "in" and "out", and copies the input points to
// "in". It then applies the Ramer-Douglas-Peucker algorithm to reduce the number of points in "in" and
// stores the result in "out". Finally, it creates a PointObj object for each point in "out", adjusts
// the coordinates, assigns a color, and adds it to the output list "tempLp", which is then returned
    private List<PointObj> reducedSplianeDP(List<Point2D.Double> list) {
        List<Point2D.Double> in = new ArrayList<>();
        List<Point2D.Double> out = new ArrayList<>();
        List<PointObj> tempLp = new ArrayList<>();
        list.forEach((p) -> {
            in.add(new Point2D.Double(p.x, p.y));
        });
        if (in.size() > 2) {
            RamerDouglasPeuckerAlgorithm.ramerDouglasPeucker(in, ramerDouglasPeuckerAlgorithm, out);
        }
        out.forEach((dp) -> {
            tempLp.add(new PointObj(new Point((int) dp.x - (Venus3D.getScreenW() / 2), (int) dp.y - ((Venus3D.getScreenH() / 2) - 35)), new Point(0, 0), new Point(0, 0), colorArray[color]));
        });
        return tempLp;
    }//end reducedSplianeDP

// The below code is a method in Java that creates arrays of points representing the positions of
// Earth, Venus, and Venus's moon over time. It takes in four VenusPoint objects representing the
// initial positions of Earth, Venus, Venus's moon, and the Sun, as well as a rotation angle and a
// ratio. It uses a while loop to calculate the positions of Earth, Venus, and Venus's moon at each
// time step, based on their initial positions and the rotation angle and ratio. It then uses a method
// called reducedSplianeDP to calculate a reduced spline of the Venus points.
    private void createArrays(VenusPoint E, VenusPoint S, VenusPoint V, VenusPoint Vm, double sang, Point2D.Double ratio) {
        boolean stop = true;
        int i = 0;
        while (stop) {
            //sun rotation fixed distance
            Earth.add(Utility.convertPoint((ratio.x * i) + sang, S.getP().x, S.getP().y, E.getP().x, E.getP().y));
            //venus roatation not fixed diatance
            Point2D.Double vv = Utility.convertPoint((ratio.y * i) + sang, V.getP().x, V.getP().y, S.getP().x, S.getP().y);
            Venus.add(Utility.convertPoint((ratio.x * i) + sang, vv.x, vv.y, E.getP().x, E.getP().y));

            double vmx = Vm.getP().x - V.getP().x, vmy = Vm.getP().y - V.getP().y;
            Point2D.Double vvv = Utility.convertPoint((2 * i) + sang, vv.x + vmx, vv.y + vmy, vv.x, vv.y);
            VMoon.add(Utility.convertPoint((ratio.x * i) + sang, vvv.x, vvv.y, E.getP().x, E.getP().y));

            if (Earth.size() > 2 && Venus.get(0).equals(Venus.get(Venus.size() - 1))) {
                stop = false;
            }
            i++;
        }
        lp = reducedSplianeDP(Venus);
    }//end createArrays

// The below code is a Java method called "rotate" that takes in a list of PointObj objects, a boolean
// value indicating whether to rotate clockwise or counterclockwise, and three double values
// representing the degrees of rotation around the x, y, and z axes.
    private void rotate(List<PointObj> lp, boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        // System.out.println(xDegrees + " " + yDegrees + " " + zDegrees);
        rotX += xDegrees;
        rotY += yDegrees;
        rotZ += zDegrees;
        //System.out.println(rotX + " " + rotY + " " + rotZ);

        lp.stream().map((p) -> {
            PointConverter.rotateAxisX(p, CW, xDegrees);
            return p;
        }).map((p) -> {
            PointConverter.rotateAxisY(p, CW, yDegrees);
            return p;
        }).forEachOrdered((p) -> {
            PointConverter.rotateAxisZ(p, CW, zDegrees);
        });
    }//end rotate

// The below code is implementing the bubble sort algorithm to sort a list of Pointer objects based on
// their pointer values. It repeatedly compares adjacent elements in the list and swaps them if they
// are in the wrong order, until the list is sorted in ascending order.
    public static void bubbleSort(List<Pointer> ps) {
        boolean sorted = false;
        Pointer temp;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < ps.size() - 1; i++) {
                if (ps.get(i).getPointer() > ps.get(i + 1).getPointer()) {
                    temp = ps.get(i);
                    ps.set(i, ps.get(i + 1));
                    ps.set(i + 1, temp);
                    sorted = false;
                }
            }
        }
    }//end bubbleSort

}// end class Venus3D

