package game;

import utilities.JEasyFrameFull;
import utilities.Vector2D;

import java.awt.*;
import java.util.List;
import java.util.Random;

public abstract class GameObject {

    public boolean dead = false;
    protected Image image;

    protected Vector2D s, v; // position and velocity
    protected Vector2D d; // direction (unit vector)
    protected int radius;
    protected long aliveTime;
    protected static PlayerShip player;

    abstract void draw(Graphics2D g);
    abstract void update(List<GameObject> list);
    abstract void hit();

    public boolean overlap(GameObject other){
        if (this.s.dist(other.s) < (this.radius / 2 + other.radius / 2))
            return true; // dummy
        return false;
    }

    public void collisionHandling(GameObject other) {
        if (this instanceof Ship && other instanceof Ship)
            return;

        // not the same class and they overlap
        if (this.getClass() != other.getClass() && this.overlap(other)) {

            GameObject bullet = null;
            if (this instanceof Bullet)
                bullet = this;
            else if (other instanceof Bullet)
                bullet = other;

            if (bullet != null && bullet.aliveTime < 100)
                return;

            if (!this.dead && !other.dead) {
                this.hit();
                other.hit();
            }
        }
    }
}
