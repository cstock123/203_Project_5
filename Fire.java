import processing.core.PImage;

import java.util.List;

public class Fire extends AnimatedEntity {

    public Fire(Point position,
                List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


}
