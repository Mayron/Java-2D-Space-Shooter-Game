package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Action extends KeyAdapter {
    public int thrust = 0; // 0 = off, 1 = on
    public int turn = 0; // -1 = left turn, 0 = no turn, 1 = right turn
    public boolean shoot = false;
    public boolean blast = false; // players special blast attack!
}
