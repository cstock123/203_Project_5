import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class MinerNotFull extends MovableEntity{

    private  int resourceLimit;
    private  int resourceCount;

    public MinerNotFull(Point position, List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(), Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, createAnimationAction(0), getAnimationPeriod());
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            MinerFull miner = Loader.createMinerFull(getPosition(), getImages(), resourceLimit,
                    getActionPeriod(), getAnimationPeriod());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    //movetonotfull
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
        }
    }
}