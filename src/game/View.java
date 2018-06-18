package game;

import utilities.ImageManager;
import utilities.JEasyFrameFull;
import utilities.Vector2D;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;

public class View extends JComponent {

    public Game game;
    private Image[][] background;
    private static double BG_WIDTH = 0;
    private static double BG_HEIGHT = 0;
    private static final double MAP_MARGIN = 0.65;

    @Override
    public void paintComponent(Graphics o) {
        Graphics2D g = (Graphics2D) o;
        AffineTransform t0 = g.getTransform();

        drawBackground(g, t0);

        // Draw all game objects:
        synchronized (Game.class) {
            ListIterator<GameObject> it = game.objects.listIterator();
            while (it.hasNext()) {
                try {
                    it.next().draw(g);
                } catch (ConcurrentModificationException e) {}
            }
        }

        g.setTransform(t0);
        g.setColor(Color.YELLOW);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.drawString("Lives: " + game.getPlayer().getLives(), 20, 25);

        g.drawString("Time: " + game.getTime(), 20, 50);

        g.drawString("Level: " + LevelManager.getLevel(), 20, 75);
    }

    // controls the background based on camera
    public void drawBackground(Graphics2D g, AffineTransform t0) {
        AffineTransform t = new AffineTransform();

        double xlower = Math.max(game.getPlayerX() - (getWidth() * 0.5), -(BG_WIDTH * MAP_MARGIN));
        double ylower = Math.max(game.getPlayerY() - (getHeight() * 0.5), -(BG_HEIGHT * MAP_MARGIN));

        double xupper = Math.min(game.getPlayerX() + (getWidth() * 0.5), (BG_WIDTH * MAP_MARGIN));
        double yupper = Math.min(game.getPlayerY() + (getHeight() * 0.5), (BG_HEIGHT * MAP_MARGIN));

        double xTrans = Math.max(xlower, game.getPlayerX()); // LOWER CHECK
        if (xTrans != xlower)
            xTrans = Math.min(xupper, xTrans); // UPPER CHECK

        double yTrans = Math.max(ylower, game.getPlayerY()); // LOWER CHECK
        if (yTrans != ylower)
            yTrans = Math.min(yupper, yTrans); // UPPER CHECK

        // follow camera:
        g.translate(-xTrans + (getWidth() / 2), -yTrans + (getHeight() / 2));

        // draw background:
        for (int r = 0; r < background.length; r++){
            for (int c = 0; c < background[r].length; c++){
                t.translate((BG_WIDTH * (c - 1)), (BG_HEIGHT * (r - 1)));
                g.drawImage(background[r][c], t, null);
                t.setTransform(t0);
            }
        }
    }

    public View(Game game) {
        this.game = game;
        try {
            background = new Image[][]{
                    {ImageManager.loadImage("Background_TopLeft.jpg"),
                        ImageManager.loadImage("Background_TopRight.jpg")},
                    {ImageManager.loadImage("Background_BottomLeft.jpg"),
                        ImageManager.loadImage("Background_BottomRight.jpg")},
            };
            BG_HEIGHT = ImageManager.getImage("Background_TopLeft.jpg").getHeight(null);
            BG_WIDTH = ImageManager.getImage("Background_TopLeft.jpg").getWidth(null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(JEasyFrameFull.WIDTH, JEasyFrameFull.HEIGHT);
    }

    // returns the size of one tile on the map.
    public static Vector2D getMapTileSize() {
        return new Vector2D(BG_WIDTH, BG_HEIGHT);
    }
}
