import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class Vein extends ActivityEntity{

    private  static final Random rand = new Random();

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(Point position, List<PImage> images, int actionPeriod) {
        super(position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent()) {
            Ore ore = Loader.createOre(ORE_ID_PREFIX,
                                        openPt.get(),
                                        ORE_CORRUPT_MIN + rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                                        imageStore.getImageList(Loader.ORE_KEY()));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                getActionPeriod());
    }
}