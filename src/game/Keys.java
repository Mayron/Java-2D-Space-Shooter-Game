package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class is only for the user but an Action can be applied to any object
 */
public class Keys extends KeyAdapter {

    private Action playerAction; // the action state of an object (the player ship)

    public Keys(Action playerAction) {
        this.playerAction = playerAction;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                playerAction.thrust = 1;
                break;
            case KeyEvent.VK_LEFT:
                playerAction.turn = -1;
                break;
            case KeyEvent.VK_RIGHT:
                playerAction.turn = 1;
                break;
            case KeyEvent.VK_SPACE:
                playerAction.shoot = true;
                break;
            case KeyEvent.VK_1:
                playerAction.blast = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                playerAction.thrust = 0;
                break;
            case KeyEvent.VK_LEFT:
                playerAction.turn = 0;
                break;
            case KeyEvent.VK_RIGHT:
                playerAction.turn = 0;
                break;
            case KeyEvent.VK_SPACE:
                playerAction.shoot = false;
                break;
        }
    }
}
