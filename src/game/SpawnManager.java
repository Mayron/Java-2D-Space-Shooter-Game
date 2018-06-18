package game;

import java.util.ArrayList;
import java.util.List;

public class SpawnManager extends Thread {
    private List<Spawner> spawners;
    private LevelManager levelManager;
    private static boolean newSpawns = false;
    private final int SLEEP_TIME = 400;

    public void run() {
        while (true) {
            levelManager.update();
            for (Spawner s : spawners) {
                s.update(levelManager.getLimit(s.getType()));
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {}
        }
    }

    public SpawnManager() {
        levelManager = new LevelManager();
        spawners = new ArrayList<Spawner>();

        spawners.add(new Spawner("Tier1Ship"));
        spawners.add(new Spawner("Tier2Ship"));

        this.start(); // start thread!

        // try new objects later on when finished developing..
        //spawners.add(new Spawner("PowerPickUp"));
        //spawners.add(new Spawner("ShieldPickUp"));
    }

    public List<GameObject> collect() {
        if (newSpawns) {
            List<GameObject> collected = new ArrayList<GameObject>();
            for (Spawner s : spawners)
                collected.addAll(s.getNewSpawns());
            newSpawns = false;
            return collected;
        }
        return null;
    }

    public static boolean hasNewSpawns() {
        return newSpawns;
    }

    public static void setNewSpawns(boolean value) {
        newSpawns = value;
    }

}
