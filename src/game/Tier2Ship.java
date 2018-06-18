package game;

import utilities.ImageManager;
import utilities.Vector2D;

import java.util.List;

public class Tier2Ship extends EnemyShip {

    public Tier2Ship() {
        this.action = new Action();
        images[0] = "T2EnemyShip1.png";
        images[1] = "T2EnemyShip2.png";
        image = ImageManager.getImage(images[0]);
        this.radius = image.getWidth(null);
        this.lives = 2;

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
        return 150;
    }
}
