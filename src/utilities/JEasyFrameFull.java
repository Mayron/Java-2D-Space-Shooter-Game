package utilities;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import javax.swing.JFrame;


public class JEasyFrameFull extends JFrame {
    public final static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public final static GraphicsDevice device = env.getScreenDevices()[0];
    public static final Rectangle RECTANGLE = device.getDefaultConfiguration().getBounds();
    public static final int WIDTH = RECTANGLE.width;
    public static final int HEIGHT = RECTANGLE.height;

    public Component comp;

    public JEasyFrameFull(Component comp, String title) {
        super(title);
        this.comp = comp;
        getContentPane().add(BorderLayout.CENTER, comp);
        comp.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setUndecorated(true);
        pack();
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();

        // a custom cursor with no visible image.
        Cursor hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(2, 2, BufferedImage.TRANSLUCENT), new Point(0, 0), "");

        // Set the blank cursor to the JFrame.
        getContentPane().setCursor(hiddenCursor);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                    setUndecorated(false); // has to be called before setVisible
                    setVisible(true);
                    getContentPane().setCursor(Cursor.getDefaultCursor());
                }
            }
        });
    }
}
