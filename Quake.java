import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position,
                 List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod, QUAKE_ANIMATION_REPEAT_COUNT);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
