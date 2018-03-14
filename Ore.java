import java.util.List;
import java.util.Random;

import processing.core.PImage;

public class Ore extends ActivityEntity {

    private  static final Random rand = new Random();

    private static final String BLOB_KEY = "blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;


    public Ore(String id, Point position, List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = Loader.createOreBlob(pos, getActionPeriod() / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }
}