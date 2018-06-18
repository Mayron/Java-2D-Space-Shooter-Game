package game;

import utilities.Vector2D;

import java.awt.*;
import java.util.List;

public class Asteroid extends GameObject {

    private static final int SIZE = 10;
    private static final int MIN_SPEED = 25;
    private static final int MAX_SPEED = 75;
    private static final int RADIUS = 20;

    @Override
    public void draw(Graphics2D g) {
        int x = (int) s.x;
        int y = (int) s.y;
        g.setColor(Color.red);
        g.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }

    @Override
    void update(List<GameObject> list) {
        s.add(v, Constants.DT);
        s.wrap(Game.getScreenWidth(), Game.getScreenHeight());
    }

    public Asteroid(double sx, double sy, double vx, double vy) {
        s = new Vector2D(sx, sy);
        v = new Vector2D(vx, vy);
    }

    public static void createRandom() {
        /*double vx = Math.random() * MAX_SPEED + MIN_SPEED;
        vx = (Math.random() >= 0.5) ? vx : -(vx);
        double vy = Math.random() * MAX_SPEED + MIN_SPEED;
        vy = (Math.random() >= 0.5) ? vy : -(vy);
        return new Asteroid(Math.random() * Game.getScreenWidth(), Math.random() * Game.getScreenHeight(), vx, vy);*/
    }

    @Override
    public void hit() {};


}
