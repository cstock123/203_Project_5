import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends MoveableEntity {
    private String id;
    private int resourceCount;
    private int resourceLimit;

    public MinerNotFull(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod, int repeatCount) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
        this.id = id;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = world.findNearest(getPosition(), Ore.class);

        Optional<Entity> fireTarget = world.findNearest(getPosition(), Fire.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), actionPeriod());
        }
    }

    private void transformToFire(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        ActiveEntity fireMiner = new MinerOnFire(id, getPosition(), imageStore.getImageList("minerFire"), resourceLimit,  actionPeriod(), animationPeriod(), 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(fireMiner);
        fireMiner.scheduleActions(scheduler, world, imageStore);
    }

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            ActiveEntity miner = new MinerFull(id, getPosition(), images(),
                    resourceLimit, actionPeriod(), animationPeriod(), 0);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
