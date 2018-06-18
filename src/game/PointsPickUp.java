package game;


public class PointsPickUp extends PickUp {
    private boolean pickedUp = false;

    public PointsPickUp()  {
        player.score += 10;
    }

    @Override
    public void hit() {}

    @Override
    public void use() {

    }
}
