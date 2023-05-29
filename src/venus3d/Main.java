
package venus3d;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * The Main class creates a JFrame and adds a Venus3D object to it.
 */
public class Main {
        public static void main(String[] args) {
        JFrame frame = new JFrame("PaintSurface Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.add(new Venus3D());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    } 
}//end Main

