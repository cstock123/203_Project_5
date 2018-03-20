import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerOnFire extends MoveableEntity {
    private String id;
    private int resourceLimit;

    public MinerOnFire(String id, Point position,
                     List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod, int repeatCount) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> waterTarget = world.findNearest(getPosition(),
                Water.class);

        if (waterTarget.isPresent() && moveTo(world, waterTarget.get(), scheduler)) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    actionPeriod());
        }
    }

    private void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        ActiveEntity miner = new MinerNotFull(id, getPosition(), imageStore.getImageList("miner"), resourceLimit, 0, 1000, animationPeriod(), 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
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
