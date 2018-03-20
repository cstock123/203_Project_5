import processing.core.PImage;

import java.util.List;

public class Character extends MoveableEntity{
    private Inventory inventory;

    public Character(Point position, List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount,
                        Inventory inventory) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
        this.inventory = inventory;
    }

    public void executeActivity(WorldModel world, ImageStore store, EventScheduler eventScheduler) {
    }

    public Inventory getInventory() {
        return inventory;
    }
}
