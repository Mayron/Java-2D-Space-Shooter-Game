package game;

public class ShieldPickUp extends PickUp {
    private int strength;
    public static final int RESPAWN_SHIELD = 100;
    public static final int STANDARD_SHIELD = 3;
    public static final int STRONG_SHIELD = 5;

    public ShieldPickUp(int type) {
        this.strength = type;
    }

    @Override
    public void hit() {

    }

    @Override
    public void use() {

    }
}
