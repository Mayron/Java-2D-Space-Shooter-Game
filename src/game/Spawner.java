package game;

import utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spawner {
    private int totalActive;
    private List<GameObject> newSpawns;
    private String type;

    private int[] pattern = new int[]{0, 4, 2, 3, 1, 2, 4, 3};
    public static int cursor = 0;
    private static final double SPAWN_COOLDOWN = 2.0;
    public static double cooldown = 2.0;
    private static int level = 0;

    //Updates time and limit and scans
    public void update(int limit) {

        if (level < LevelManager.getLevel()) {
            totalActive = 0;
            level = LevelManager.getLevel();
        }

        int numOfSpawns = LevelManager.getTotalObjects() - totalActive;
        numOfSpawns = Math.min(numOfSpawns, 5 - pattern[cursor]);
        if (cooldown < 0.0) {
            for (int n = 0; n < numOfSpawns; n++) {
                if (totalActive < limit) {
                    totalActive++;

                    // Strings for switch statements are not working with IntelliJ with JDK 7 and 8:
                    String[] names = {"Tier1Ship", "Tier2Ship"}; // , "PowerPickUp", "ShieldPickUp"
                    int id;
                    for (id = 0; id < names.length; id++)
                        if (names[id].equals(type))
                            break;
                    GameObject spawnedObject;

                    switch (id) {
                        case 0: // "Tier1Ship"
                            spawnedObject = new Tier1Ship();
                            break;
                        case 1: // "Tier2Ship"
                            spawnedObject = new Tier2Ship();
                            break;
                   /* case 2: // "PowerPickUp"
                        spawnedObject = new PowerPickUp();
                        break;
                    case 3: // "ShieldPickUp"
                        spawnedObject = new ShieldPickUp();
                        break;*/
                        default:
                            spawnedObject = new Tier1Ship();
                            break;
                    }

                    newSpawns.add(spawnedObject);
                }

                SpawnManager.setNewSpawns(true);
                cursor++;
                cursor = (cursor == pattern.length) ? 0 : cursor;
            }
            cooldown = SPAWN_COOLDOWN;
        } else {
            cooldown -= 0.1;
        }
    }

    public Spawner(String type) {
        this.type = type;
        newSpawns = new ArrayList<GameObject>();
    }

    public List<GameObject> getNewSpawns() {
        List<GameObject> temp = new ArrayList<GameObject>(newSpawns);
        newSpawns = new ArrayList<GameObject>();
        return temp;
    }

    public String getType() { return this.type; }
}
