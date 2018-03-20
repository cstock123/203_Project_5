import processing.core.PImage;

import java.util.List;

public class Ore extends ActiveEntity
{

    private static final String BLOB_KEY = "blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(Point position, List<PImage> images, int actionPeriod)
    {
        super(position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        AnimatedEntity blob = new OreBlob(pos, imageStore.getImageList(BLOB_KEY), actionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand().nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN), 0);

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
}
