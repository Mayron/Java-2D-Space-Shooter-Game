package game;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class PickUp extends GameObject {

    @Override
    public void draw(Graphics2D g) {

    }

    public abstract void use();

    @Override
    void update(List<GameObject> list) {
        // rotate around for super fun
    }
}
