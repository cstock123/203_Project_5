import java.util.List;
import java.util.Optional;
import processing.core.PImage;

public class Character extends MovableEntity {

    public Character(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //Point nextPos = new Point(getPosition().getX() + 1, getPosition().getY());
        //moveTo(world, this, scheduler);
    }

    /*
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        Point nextPos = new Point(getPosition().getX() + 1, getPosition().getY());

        if (!getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }
            world.moveEntity(this, nextPos);
        }
        return false;
    }
    */

    /*
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, createAnimationAction(0),
                getAnimationPeriod());
    }
    */
}
