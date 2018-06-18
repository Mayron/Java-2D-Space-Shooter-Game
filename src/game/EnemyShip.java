package game;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public abstract class EnemyShip extends Ship {

    protected static List<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    protected final int MAX_PLAYER_RANGE = 800; // How far the ship is to player before it chases (800)
    protected final int MIN_PLAYER_RANGE = 500; // How close the ship is allowed to player (600)
    protected final int MAX_SHOOT_RANGE = 500; // MAX range before it can shoot player ( 500)

    private final int MIN_RANGE_APART = 200; // how far each enemy ship should be away from each other

    private boolean outOfMap = false;
    private Vector2D destinationPosition;
    private double newDestCooldown = -1;
    protected boolean outOfCombat; // whether chasing the player

    public abstract void shoot(List<GameObject> list);
    public abstract double getTopSpeed();

    @Override
    public void update(List<GameObject> list) {

        double distanceFromPlayer = this.s.dist(player.s);
        if (newDestCooldown <= 0) {
            // Scale accuracy depending on how far player is:
            if (distanceFromPlayer < MAX_PLAYER_RANGE) {
                destinationPosition = player.s;
                if (distanceFromPlayer < 300) {
                    newDestCooldown = 0.0;
                } else {
                    double delay = ((distanceFromPlayer / MAX_PLAYER_RANGE) * 10);
                    delay *= 0.41;
                    newDestCooldown = Math.min(delay, 5); // 5 is the limit
                }
            } else {
                destinationPosition = getDestinationPosition();
                newDestCooldown = 100;
            }
        } else
            newDestCooldown -= 1;

        action.shoot = (distanceFromPlayer < MAX_SHOOT_RANGE);

        // rotate to face
        Vector2D facingDirection = new Vector2D(s);
        facingDirection.sub(destinationPosition);

        double x = facingDirection.x;
        double y = facingDirection.y;
        facingDirection.set(-x, -y);

        double thetaDifference = facingDirection.theta() - d.theta();

        if (Math.abs(thetaDifference) > 0.06) {
            action.turn = (thetaDifference < 0) ? -1 : 1;
            d.rotate(Constants.DT * action.turn * STEER_RATE);
        } else
            action.turn = 0;

        if (distanceFromPlayer > MIN_PLAYER_RANGE) {
            action.thrust = 1;
            image = ImageManager.getImage(images[1]);
        } else {
            action.thrust = 0;
            image = ImageManager.getImage(images[0]);

            // random movement!!
            if (distanceFromPlayer < MIN_PLAYER_RANGE * 0.7) {
                double add = (d.x * player.d.x + d.y* player.d.y);
                s.add(add, Constants.DT);
            }
        }

        v.add(d, getTopSpeed() * Constants.DT * action.thrust);
        v.mult(LOSS);
        s.add(v, Constants.DT);

        if (!outOfMap) {
            Vector2D mapSize = View.getMapTileSize();
            s.wrap(-mapSize.x, -mapSize.y, mapSize.x, mapSize.y);
        }

        //comment this out to stop shooting:
        shoot(list);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        AffineTransform t = new AffineTransform(); // creates a new point of drawing (paper with no width or height)
        t.translate(-image.getWidth(null) / 2.0, -image.getHeight(null) / 2.0); // To get the center of the ship!!
        AffineTransform t0 = g.getTransform();

        g.translate(s.x, s.y); // decides where to start drawing on g
        g.setColor(Color.YELLOW);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        g.drawString("" + lives, 0, 0);
        g.setTransform(t0); // reset the "pen point" back to the default center
    }

    private Vector2D getDestinationPosition() {
        Vector2D dest = new Vector2D(player.s);

        double distanceFromPlayer = this.s.dist(dest);
        outOfCombat = (distanceFromPlayer > MAX_PLAYER_RANGE);

        // check if out of map. If it is, move to player without combat mode
        if (outOfCombat) {
            Vector2D tileSize = View.getMapTileSize();
            if (this.s.x < -tileSize.x || this.s.x > tileSize.x ||
                    this.s.y < -tileSize.y || this.s.y > tileSize.y) {
                outOfMap = true;
            } else {
                outOfMap = false;
            // Go somewhere close but not directly to player (not chasing)
                if (distanceFromPlayer > MAX_PLAYER_RANGE) {
                    Vector2D bounds = new Vector2D();
                    bounds.x = MAX_PLAYER_RANGE + (player.s.x * 0.5);
                    bounds.y = MAX_PLAYER_RANGE + (player.s.y * 0.5);
                    //dest = getRandomPosition(bounds, 300);
                    dest = new Vector2D(player.s);
                }
             }
        }

        // if player is in map region and needs to pick a random direction to travel in.
        // Should move away from the edges of the map while avoiding other ships.

        // scan for friendly ships close to this ship
        List<EnemyShip> ships = getShipsCloseBy();
        if (!ships.isEmpty()) {
            for (EnemyShip ship : ships) {
                handleCollisionCourse(ship);
            }
        }

        return dest;
    }

    private List<EnemyShip> getShipsCloseBy() {
        synchronized (Ship.class) {
            List<EnemyShip> ships = new ArrayList<EnemyShip>();
            for (EnemyShip ship : enemyShips) {
                if (!ship.dead && !(this.s.equals(ship.s))) {
                    if (this.s.dist(ship.s) < MIN_RANGE_APART) {
                        ships.add(ship);
                    }
                }
            }
            return ships;
        }
    }

    // checks if the two ships will collide
    private void handleCollisionCourse(EnemyShip ship) {
        Vector2D coll = new Vector2D(ship.s);
        coll.sub(this.s);
        coll.normalise();
        Vector2D tang = new Vector2D(-coll.y, coll.x);

        this.v = this.v.proj(tang);
        this.v.add(ship.v.proj(coll));

        ship.v = ship.v.proj(tang);
        ship.v.add(this.v.proj(coll));
    }

    public static List<EnemyShip> getEnemyShips() { return enemyShips; }

    protected static Vector2D getRandomPosition(Vector2D tileSize, int padding) {
        int spawnID;
        do {
            spawnID = (int)(Math.random() * 10);
        } while (!(spawnID < 8));

        // double xmax, double ymax, double xmin, double ymin
        switch (spawnID) {
            case 0: // n < -X and n < -Y
                return Vector2D.createRandomVector2D(
                        -tileSize.x, // MAX X
                        -tileSize.y, // MAX Y
                        -tileSize.x - padding,  // MIN X
                        -tileSize.y - padding); // MIN Y
            case 1: // n < -Y
                return Vector2D.createRandomVector2D(
                        tileSize.x + padding, // MAX X
                        -tileSize.y, // MAX Y
                        -tileSize.x - padding,  // MIN X
                        -tileSize.y - padding); // MIN Y
            case 2: // n < -Y and n > X
                return Vector2D.createRandomVector2D(
                        tileSize.x + padding, // MAX X
                        -tileSize.y, // MAX Y
                        tileSize.x,  // MIN X
                        -tileSize.y - padding); // MIN Y
            case 3: // n > X
                return Vector2D.createRandomVector2D(
                        tileSize.x + padding, // MAX X
                        tileSize.y + padding, // MAX Y
                        tileSize.x,  // MIN X
                        -tileSize.y - padding); // MIN Y
            case 4:  // n > X and n > Y
                return Vector2D.createRandomVector2D(
                        tileSize.x + padding, // MAX X
                        tileSize.y + padding, // MAX Y
                        tileSize.x,  // MIN X
                        tileSize.y); // MIN Y
            case 5: // n > Y
                return Vector2D.createRandomVector2D(
                        tileSize.x + padding, // MAX X
                        tileSize.y + padding, // MAX Y
                        -tileSize.x - padding,  // MIN X
                        tileSize.y); // MIN Y
            case 6: // n > Y and n < -X
                return Vector2D.createRandomVector2D(
                        -tileSize.x, // MAX X
                        tileSize.y + padding, // MAX Y
                        -tileSize.x - padding,  // MIN X
                        tileSize.y); // MIN Y
            case 7:  // n < -X
                return Vector2D.createRandomVector2D(
                        -tileSize.x, // MAX X
                        tileSize.y + padding, // MAX Y
                        -tileSize.x - padding,  // MIN X
                        -tileSize.y - padding); // MIN Y
            default:
                return null;
        }
    }

    @Override
    public void hit() {
        this.lives -= 1;
        if (this.lives == 0) {
            dead = true;
            //enemyShips.remove(this);
        }
    }

}
