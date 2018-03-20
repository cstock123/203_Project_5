import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Vein extends ActiveEntity
{
    private static final String ORE_KEY = "ore";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(Point position, List<PImage> images, int actionPeriod)
    {
        super(position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent())
        {
            ActiveEntity ore = new Ore(openPt.get(), imageStore.getImageList(ORE_KEY), ORE_CORRUPT_MIN +
                            rand().nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod());
    }
}
