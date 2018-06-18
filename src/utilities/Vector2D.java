package utilities;

import java.util.Random;

// mutable 2D vectors
public final class Vector2D {
    private static Random r = new Random();

    // fields
    public double x, y;

    // construct a null vector
    public Vector2D() {
        this(0, 0);
    }

    // construct a vector with given coordinates
    public Vector2D(double x, double y) { this.x = x; this.y = y; }

    // construct a vector that is a copy of the argument
    public Vector2D(Vector2D v) { x = v.x; y = v.y; }

    // set coordinates
    public void set(double x, double y) { this.x = x; this.y = y; }

    // set coordinates to argument vector coordinates
    public void set(Vector2D v) { x = v.x; y = v.y; }

    // compare for equality (needs to allow for Object type argument...)
    public boolean equals(Object o) {
        Vector2D v = (Vector2D) o;
        return (x == v.x && y == v.y);
    }

    // magnitude (= "length") of this vector
    public double mag() {
        return Math.hypot(x, y);
    }

    // angle between vector and horizontal axis in radians
    public double theta() {
        return Math.atan2(y, x);
    }

    // String for displaying vector as text
    public String toString() {
        return "X: " + x + ", " + "Y: " + y;
    }

    // add argument vector
    public void add(Vector2D v) {
        x += v.x; y += v.y;
    }

    // add coordinate values
    public void add(double x, double y) {
        this.x += x; this.y += y;
    }

    // weighted add - frequently useful
    public void add(Vector2D v, double fac) {
        x += (v.x * fac); y += (v.y * fac);
    }

    // multiply with factor
    public void mult(double fac) {
        x *= fac; y *= fac;
    }

    // "wrap" vector with respect to given positive values w and h
    // method assumes that x >= -w and y >= -h
    public void wrap(double w, double h) {
        x = (x + w) % w;
        y = (y + h) % h;
    }

    // "wrap" vector with respect to given positive values w and h
    // method assumes that x >= -w and y >= -h
    public void wrap(double minw, double minh, double maxw, double maxh) {
        if (x > maxw)
            x = minw;
        else if (x < minw)
            x = maxw;
        if (y >= maxh)
            y = minh;
        else if (y < minh)
            y = maxh;
    }

    // rotate by angle given in radians
    public void rotate(double theta) {
        double nx = x * Math.cos(theta) - y * Math.sin(theta);
        double ny = x * Math.sin(theta) + y * Math.cos(theta);
        x = nx;
        y = ny;
    }

    // scalar product with argument vector
    public double scalarProduct(Vector2D v) {
        return (x * v.x) + (y * v.y);
    }

    // distance to argument vector
    public double dist(Vector2D v) {
        //return v.mag() - mag(); // fails
        return Math.hypot(v.x - x, v.y - y); // works
    }

    // normalise vector so that mag becomes 1
    // direction is unchanged
    public void normalise() {
        double mag = mag();
        x /= mag;
        y /= mag;
    }

    public void sub(Vector2D v) {
        x -= v.x;
        y -= v.y;
    }

    // returns the vector projection in some direction d
    public Vector2D proj(Vector2D d) {
        Vector2D result = new Vector2D(d);
        result.mult(this.scalarProduct(d));
        return result;
    }

    public static Vector2D createRandomVector2D(double xmax, double ymax) {
        double x = Math.random() * xmax;
        double y = Math.random() * ymax;
        while (x > xmax)
            x -= xmax;
        while (y > ymax)
            y -= ymax;
        return new Vector2D(x, y);
    }

    // MUST BE LESS THAN XMAX AND X MIN!!
    public static Vector2D createRandomVector2D(double xmax, double ymax, double xmin, double ymin) {
        double x, y;

        do {
            x = xmin + ((xmax - xmin) * r.nextDouble());
        } while (!(x < xmax && x > xmin));

        do {
            y = ymin + ((ymax - ymin) * r.nextDouble());
        } while (!(y < ymax && y > ymin));

        return new Vector2D(x, y);
    }
}