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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
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
    private final Utility u;
    private final Spline sp;

    //components
    private JPanel radioPanel;
    private JScrollPane jsp;
    private final PaintSurface paintSurface;
    //list varables
    private List<ArrayList<PointObj>> arrayOfArrays = new ArrayList<>();
    private List<ArrayList<PointObj>> snapArrayOfArrays = new ArrayList<>();
    private List<Pointer> pointerArray = new ArrayList<>();

    private BufferedImage canvas;
    JLabel canvasLabel;
    Graphics2D g2d;

    //boolean varables
    private boolean mouseDown, selected;
    private Point mouse, snapMouse;
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
    //point varables
    private Point staticDiff = new Point(0, 0);
    private Point diff = new Point(0, 0);
    //int varables

    private Point3D ViewVector = new Point3D(300, 0, 0);

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

    private int rotateWeel = 0;

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

    public Venus3D() {
        u = new Utility(screenW, screenH);
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

    }

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
    }

    private List<Polygon3D> craeatePolyList(List<ArrayList<PointObj>> pol, int lineW) {
        List<Polygon3D> pl = new ArrayList<>();
        for (int i = 0; i < pol.size(); i++) {
            for (int j = 0; j < pol.get(i).size() - 1; j++) {
                pl.add(
                        new Polygon3D(
                                pol.get(i).get(j).getP3(),
                                pol.get(i).get(j + 1).getP3(),
                                new Point3D(
                                        pol.get(i).get(j).getP3().getX(),
                                        pol.get(i).get(j).getP3().getY(),
                                        pol.get(i).get(j).getP3().getZ() + lineW
                                )
                        )
                );
                pl.add(
                        new Polygon3D(
                                pol.get(i).get(j + 1).getP3(),
                                pol.get(i).get(j).getP3(),
                                new Point3D(
                                        pol.get(i).get(j + 1).getP3().getX(),
                                        pol.get(i).get(j + 1).getP3().getY(),
                                        pol.get(i).get(j + 1).getP3().getZ() - lineW
                                )
                        )
                );
            }
        }
        return pl;
    }// end craeatePolyList

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
                        rotateWeel--;
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
                    snapArrayOfArrays = arrayOfArrays;
                    snapMouse = mouse;
                }//end mouseExited

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        mouseDown = false;
                        selected = false;

                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                    }//System.out.println("Middle Click!");
                    if (e.getButton() == MouseEvent.BUTTON3) {
                    }
                    staticDiff = diff;

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

    private void pintRotation(Graphics2D g2) {
        BasicStroke lw = new BasicStroke(2);
        g2.setStroke(lw);
        g2.setPaint(Color.BLACK);
        g2.drawString("X:" + rotX + ", Y:" + rotY + ", Z:" + rotZ + ",", 70, 20);
    }//end pintRotation

    private void space(Graphics2D g2) {

        int sw = 1;

        BasicStroke lw = new BasicStroke(sw);

        GeneralPath p1 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        /*
        p1.moveTo(space3d.get(0).getXy().x, space3d.get(0).getXy().y);
        p1.lineTo(space3d.get(1).getXy().x, space3d.get(1).getXy().y);
        p1.lineTo(space3d.get(2).getXy().x, space3d.get(2).getXy().y);
        p1.lineTo(space3d.get(3).getXy().x, space3d.get(3).getXy().y);
        p1.lineTo(space3d.get(0).getXy().x, space3d.get(0).getXy().y);
        
         */

        p1.moveTo(space3d.get(4).getXy().x, space3d.get(4).getXy().y);
        p1.lineTo(space3d.get(5).getXy().x, space3d.get(5).getXy().y);
        p1.lineTo(space3d.get(6).getXy().x, space3d.get(6).getXy().y);
        p1.lineTo(space3d.get(7).getXy().x, space3d.get(7).getXy().y);
        p1.lineTo(space3d.get(4).getXy().x, space3d.get(4).getXy().y);
        g2.setStroke(lw);
        g2.setPaint(Color.BLUE);
        g2.draw(p1);
    }//end space

    private void axis(Graphics2D g2) {

        int sw = 5;

        BasicStroke lw = new BasicStroke(sw);

        GeneralPath p1 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        p1.moveTo(axis3d.get(0).getXy().x, axis3d.get(0).getXy().y);
        p1.lineTo(axis3d.get(1).getXy().x, axis3d.get(1).getXy().y);
        g2.setStroke(lw);
        g2.setPaint(Color.RED);
        g2.draw(p1);

        GeneralPath p2 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        p2.moveTo(axis3d.get(2).getXy().x, axis3d.get(2).getXy().y);
        p2.lineTo(axis3d.get(3).getXy().x, axis3d.get(3).getXy().y);
        g2.setStroke(lw);
        g2.setPaint(Color.GREEN);
        g2.draw(p2);

        GeneralPath p3 = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        p3.moveTo(axis3d.get(4).getXy().x, axis3d.get(4).getXy().y);
        p3.lineTo(axis3d.get(5).getXy().x, axis3d.get(5).getXy().y);
        g2.setStroke(lw);
        g2.setPaint(Color.BLUE);
        g2.draw(p3);
    }// end axis

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

    private void paintLine(Graphics2D g2, List<PointObj> lp) {
        int c = 3;
        int sw = 5;
        BasicStroke lw = new BasicStroke(10);
        Color sc = Color.BLUE;
        GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        if (!(lp.isEmpty())) {
            if (lp.size() > 0) {
                p.moveTo(lp.get(0).getXy().x, lp.get(0).getXy().y);
                for (PointObj po : lp) {
                    p.lineTo(po.getXy().x, po.getXy().y);
                }
            }
        }
        g2.setStroke(lw);
        g2.setPaint(colorArray[c]);
        g2.draw(p);
    }//end paintLine

    private void paintLinePoly(Graphics2D g2, List<Polygon3D> pl) {
        int c = 3;
        int sw = 5;
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
            Point3D cent = pli.centroid();
            Point3D Normal = pli.calcNormal();
            Point3D np3 = new Point3D(
                    Normal.getX(),
                    Normal.getY(),
                    Normal.getZ()
            );
            Point nnn = PointConverter.convertPoint(np3);
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
    }//end reducedSpliane

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
    }

    private void painPattern(Graphics2D g2) {

        Shape s;
        s = Utility.drawEllipse((int) vpa[0].getP().x, (int) vpa[0].getP().y, vpa[0].getR());
        g2.setColor(vpa[0].getSc());
        g2.setColor(vpa[0].getSc());
        g2.drawString("s", (int) vpa[0].getP().x - 12, (int) vpa[0].getP().y + 5);
        g2.setStroke(new BasicStroke(1));
        g2.setPaint(vpa[0].getSc());
        g2.draw(s);

        s = Utility.drawEllipse((int) vpa[1].getP().x, (int) vpa[1].getP().y, vpa[1].getR());
        g2.setColor(vpa[1].getSc());
        g2.drawString("V", (int) vpa[1].getP().x - 17, (int) vpa[1].getP().y + 5);
        g2.setStroke(new BasicStroke(1));
        g2.setPaint(vpa[1].getSc());
        g2.draw(s);

        s = Utility.drawEllipse((int) vpa[2].getP().x, (int) vpa[2].getP().y, vpa[2].getR());
        g2.setColor(vpa[2].getSc());
        g2.drawString("E", (int) vpa[2].getP().x - 19, (int) vpa[2].getP().y + 5);
        g2.setStroke(new BasicStroke(1));
        g2.setPaint(vpa[2].getSc());
        g2.draw(s);

        s = Utility.drawEllipse((int) vpa[3].getP().x, (int) vpa[3].getP().y, vpa[3].getR());
        g2.setColor(vpa[3].getSc());
        g2.drawString("Em", (int) vpa[3].getP().x - 19, (int) vpa[3].getP().y + 5);
        g2.setStroke(new BasicStroke(1));
        g2.setPaint(vpa[3].getSc());
        g2.draw(s);

        paintSpliane(g2, sp.drawSplines(lp));
    }//end painPattern

    private void createArrays(VenusPoint E, VenusPoint S, VenusPoint V, VenusPoint Vm, double sang, Point2D.Double ratio) {
        boolean stop = true;
        int i = 0;
        while (stop) {
            //sun rotation fixed distance
            Earth.add(u.convertPoint((ratio.x * i) + sang, S.getP().x, S.getP().y, E.getP().x, E.getP().y));
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

    private void order() {
        List<Pointer> pl = new ArrayList<>();
        int ac = 0, c = 0;
        arrayOfArrays.forEach((a) -> {
            a.forEach((it) -> {
                pl.add(new Pointer(counter0.intValue(), it.getP3().getZ()));
                // System.out.println(counter.get()+" "+it.getP3().z);
                // System.out.println(counter.intValue());
            });
            // System.out.println(".....................");
            counter0.incrementAndGet();

        });
        counter0.set(0);
        bubbleSort(pl);
        pl.forEach((a) -> {
            // System.out.println(a.getArrayPointer() + "   " + a.getPointer());
        });

    }//end order

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

}
