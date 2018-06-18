package game;

import utilities.ImageManager;
import utilities.Vector2D;

import java.io.IOException;
import java.util.List;

public class Tier1Ship extends EnemyShip {

    public Tier1Ship() {
        this.action = new Action();
        images[0] = "T1EnemyShip1.png";
        images[1] = "T1EnemyShip2.png";
        image = ImageManager.getImage(images[0]);
        this.radius = image.getWidth(null);

        s = getRandomPosition(View.getMapTileSize(), 50);
        v = new Vector2D(0, 0);
        d = new Vector2D(0,-1);

        synchronized (Ship.class) {
            enemyShips.add(this);
        }
    }

    @Override
    public void shoot(List<GameObject> list) {
        if (action.shoot && bulletCoolDown < 0) {
            list.add(createBullet());
            bulletCoolDown = 8;
        } else
            bulletCoolDown -= 0.2;
    }

    @Override
    public double getTopSpeed() {
        return 260;
    }
}
