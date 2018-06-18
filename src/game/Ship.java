package game;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import utilities.ImageManager;
import utilities.JEasyFrameFull;
import utilities.Vector2D;
import java.util.List;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

public abstract class Ship extends GameObject {
    public static final double STEER_RATE = Math.PI * 1.4;
    protected int lives = 1;
    // constant speed loss factor (how much the thrust slows down by over time)
    public static final double LOSS = 0.98;
    protected double bulletCoolDown = 0.0;
    protected boolean hasShield = false;
    protected String[] images = new String[2];

    public Action action; // The current action being performed by the player onto the ship

    public void draw(Graphics2D g) {
        AffineTransform t = new AffineTransform(); // creates a new point of drawing (paper with no width or height)
        t.rotate(d.theta(), 0, 0); // rotates point
        t.translate(-image.getWidth(null) / 2.0, -image.getHeight(null) / 2.0); // To get the center of the ship!!
        AffineTransform t0 = g.getTransform();

        g.translate(s.x, s.y); // decides where to start drawing on g
        g.drawImage(image, t, null); // draws the image using the point positioned in the center of the image
        g.setColor(Color.YELLOW);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g.setTransform(t0); // reset the "pen point" back to the default center
    }

    protected Bullet createBullet() {
        Vector2D bs = new Vector2D(s);
        Vector2D bv = new Vector2D(v);
        Vector2D bd = new Vector2D(d);
        bs.add(d, image.getHeight(null) * 0.7);
        bv.add(d, Bullet.INITIAL_SPEED);
        return new Bullet(bs, bv, bd);
    }
}
