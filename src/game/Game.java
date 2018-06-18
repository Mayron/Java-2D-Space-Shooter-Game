package game;

import utilities.ImageManager;
import utilities.JEasyFrameFull;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Game {
    public static List<GameObject> objects;
    private static boolean running = true;
    private PlayerShip player;
    private Keys keys;
    private static View view;
    private static SpawnManager spawnManager; // Controls the spawning of objects
    private final long START_TIME;
    private long time = 0;

    public static void main(String[] args) throws Exception {
        ImageManager.loadAllImages();
        Game game = new Game();
        view = new View(game);
        spawnManager = new SpawnManager();

        new JEasyFrameFull(view, "Assignment 1 Game").addKeyListener(game.keys);

        // Game Loop:
        int i = 0;
        while (running) {
            long time = System.currentTimeMillis();
            game.update();
            i++;
            if (i == Constants.REPAINT_SPEED) {
                view.repaint();
                i = 0;
            }
            Thread.sleep(Math.max(0, Constants.DELAY + time - System.currentTimeMillis()));
        }
        System.exit(0);
    }

    /**
     * Updates the positioning of all Game Objects
     */
    public void update() {
        time = System.currentTimeMillis() - START_TIME;
        synchronized (Game.class) {
            List<GameObject> alive = new ArrayList<GameObject>();

            if (SpawnManager.hasNewSpawns()) // check if if statement needed!
                objects.addAll(spawnManager.collect());

            for (GameObject object : objects) {
                object.update(alive);
                if (!object.dead)
                    alive.add(object);
            }
            objects.clear();
            objects.addAll(alive);

            // collision handling:
            for (int i = 0; i < objects.size(); i++) {
                for (int j = 1; j < objects.size(); j++) {
                    objects.get(i).collisionHandling(objects.get(j));
                }
            }
        }
    }

    /**
     * This should attach the key handling class (implements KeyAdapter)
     * And anything else needed to know for the Game class
     */
    public Game() {
        Action action = new Action();
        keys = new Keys(action);
        objects = new ArrayList<GameObject>();
        player = new PlayerShip(action);
        objects.add(player);
        START_TIME = System.currentTimeMillis();
    }

    public static double getScreenWidth() { return view.getWidth(); }
    public static double getScreenHeight() { return view.getHeight(); }

    public double getPlayerX() { return player.s.x; }
    public double getPlayerY() { return player.s.y; }

    public static void gameOver() {
        System.out.println("GAME OVER!");
        running = false;
    }

    public PlayerShip getPlayer() { return player; }

    public String getTime() {
        return String.format("%6.2f", (time / 1000.0));
    }
}
