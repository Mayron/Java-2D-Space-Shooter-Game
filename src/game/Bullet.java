package game;

import utilities.Vector2D;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.awt.*;

public class Bullet extends GameObject {

    public static final double INITIAL_SPEED = 400;
    private int ypoints[] = {-4, -4, 20, 20};
    private int xpoints[] = {2, -2, -2, 2};
    private long beforeTime;
    private final long DURATION = 2000;

    public Bullet(Vector2D s, Vector2D v, Vector2D d) {
        this.s = s;
        this.v = v;
        this.d = d;
        beforeTime = System.currentTimeMillis();
        aliveTime = 0;
    }

    @Override
    public void hit() { dead = true; };

    public void update(List<GameObject> list) {
        // position:
        s.add(v, Constants.DT);
        aliveTime += System.currentTimeMillis() - beforeTime;
        beforeTime = System.currentTimeMillis();

        if (aliveTime > DURATION)
            dead = true;
    }

    public void draw(Graphics2D g) {
        AffineTransform t = g.getTransform();
        g.translate(s.x, s.y);
        double rot = d.theta() + Math.PI / 2;
        g.rotate(rot);

        g.setColor(Color.RED);
        g.fillPolygon(xpoints, ypoints, xpoints.length);
        g.setTransform(t);
    }
}
