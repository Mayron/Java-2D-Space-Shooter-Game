package utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageManager {

    // this may need modifying
    final static String path = "media/images/";

    static Map<String, Image> images = new HashMap<String, Image>();

    public static Image getImage(String s) {
        return images.get(s);
    }

    public static Image loadImage(String fname) throws IOException {
        BufferedImage img;
        img = ImageIO.read(new File(path + fname));
        images.put(fname, img);
        return img;
    }

    public static Image loadImage(String imName, String fname) throws IOException {
        BufferedImage img = null;
        img = ImageIO.read(new File(path + fname));
        images.put(imName, img);
        return img;
    }

    public static void loadImages(String[] fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

    public static void loadImages(Iterable<String> fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

    public static void loadAllImages(){
        try {
            ImageManager.loadImage("PlayerShip1.png");
            ImageManager.loadImage("PlayerShip2.png");
            ImageManager.loadImage("T1EnemyShip1.png");
            ImageManager.loadImage("T1EnemyShip2.png");
            ImageManager.loadImage("T2EnemyShip1.png");
            ImageManager.loadImage("T2EnemyShip2.png");
        } catch (IOException e) {
            System.out.println("Failed to load images. Program terminating..");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

}
