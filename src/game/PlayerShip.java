package game;

import utilities.ImageManager;
import utilities.JEasyFrameFull;
import utilities.Vector2D;

import java.awt.*;
import java.util.List;
import java.io.IOException;

public class PlayerShip extends Ship {
    public int score = 0;
    private int lives = 10;
    private int maxBlastArea = 500;
    private int blastArea;
    private int totalBlasts = 5;
    private static final double TOP_SPEED = 320;
    private int hitPoints = 5;

    @Override
    public void update(List<GameObject> list) {
        // direction
        d.rotate(Constants.DT * action.turn * STEER_RATE);

        // velocity
        v.add(d, TOP_SPEED * Constants.DT * action.thrust);
        v.mult(LOSS);

        // position
        s.add(v, Constants.DT);

        // out of map area wrap round
        Vector2D mapSize = View.getMapTileSize();
        s.wrap(-mapSize.x, -mapSize.y, mapSize.x, mapSize.y);

        if (action.thrust > 0)
            image = ImageManager.getImage(images[1]);
        else
            image = ImageManager.getImage(images[0]);
        if (action.shoot && bulletCoolDown < 0) {
            list.add(createBullet());
            bulletCoolDown = 1;
        } else
            bulletCoolDown -= 0.2;

        if (action.blast)
            blastArea();
    }

    public PlayerShip(Action action) {
        player = this;
        blastArea = radius;
        images[0] = "PlayerShip1.png";
        images[1] = "PlayerShip2.png";
        image = ImageManager.getImage(images[0]);
        radius = image.getWidth(null);

        this.action = action;
        s = new Vector2D(0, 0);
        v = new Vector2D(0, 0);
        d = new Vector2D(0, -1);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        if (action.blast) {
            g.setColor(Color.YELLOW);
            g.drawOval((int)(s.x-blastArea / 2.0), (int)(s.y-blastArea / 2.0), blastArea, blastArea);
        }
    }

    @Override
    public void hit(){
        hitPoints -= 1;
        System.out.println("HIT PLAYER!");

        if (hitPoints < 5) {
            lives -= 1;
            hitPoints = 5;
            s.set(0, 0);
            v.set(0, 0);
            d.set(0, -1);
            // blast the area:
            action.blast = true;
            totalBlasts = 6;
            maxBlastArea = 800;
            blastArea();

            if (lives == 0) {
                dead = true;
                Game.gameOver();
            }
        }
    }

    public int getLives() { return lives; }

    public void blastArea() {
        if (totalBlasts > 0 && blastArea < maxBlastArea) {
            blastArea += 1000 * Constants.DT;
            List<EnemyShip> enemyShips = EnemyShip.getEnemyShips();
            for (GameObject object : Game.objects) {
                if (object.getClass() != player.getClass()) {
                    if (s.dist(object.s) < ((blastArea / 2.0) + (object.radius / 2.0))) {
                        object.dead = true;
                    }
                }
            }

        } else {
            action.blast = false;
            totalBlasts -= 1;
            blastArea = radius;
            maxBlastArea = 500;
        }
    }
}
