package game;

import java.util.HashMap;

public class LevelManager {
    private HashMap<String, Integer> limits = new HashMap<String, Integer>(); // limit of enemies allowed on screen at once
    private static int level = 1;
    private static int time = 0;
    private int increaseRate = 4;
    private static int totalObjects;

    public LevelManager() {
        //Setup default HashMap data:
        limits.put("Tier1Ship", 4);
        limits.put("Tier2Ship", 1);
        totalObjects = 5;
        //limits.put("PowerPickUp", 0);
        //limits.put("ShieldPickUp", 0);
    }

    public void incrementLevel() {
        time = 0;
        level += 1;
        System.out.println("NEXT LEVEL: " + level);

        increaseRate = (level + increaseRate) / 2;

        int newValue = limits.get("Tier1Ship") + increaseRate;
        limits.replace("Tier1Ship", newValue);
        totalObjects = newValue;

        newValue = limits.get("Tier2Ship") + increaseRate;
        limits.replace("Tier2Ship", newValue);
        totalObjects += newValue;

        Spawner.cooldown = 0;
        Spawner.cursor = 0;

        /*oldValue = limits.get("PowerPickUp");
        limits.replace("PowerPickUp", oldValue, oldValue + 1 + increaseRate);

        oldValue = limits.get("ShieldPickUp");
        limits.replace("ShieldPickUp", oldValue, oldValue + 1 + increaseRate);*/
    }

    public void update() {
        time += 1;
        int nextLevel = (80 * level) + (10 * level);

        if (time > nextLevel)
            incrementLevel();
    }

    public static int getLevel() {
        return level;
    }

    // Gets the maximum number of objects that can be on the map at any time during the current level.
    public int getLimit(String type) {
        return limits.get(type);
    }

    public static int getTotalObjects() { return totalObjects; }
}
