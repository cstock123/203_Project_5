import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MoveableEntity {
    private String id;
    private int resourceLimit;

    public MinerFull(String id, Point position,
                     List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod, int repeatCount) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
        this.id = id;
        this.resourceLimit = resourceLimit;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(),
                Blacksmith.class);

        Optional<Entity> fireTarget = world.findNearest(getPosition(), Fire.class);

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else if(fireTarget.isPresent() && getPosition().adjacent(fireTarget.get().getPosition())) {
            //transformToFire(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    actionPeriod());
        }
    }

    private void transformToFire(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        ActiveEntity fireMiner = new MinerOnFire(id, getPosition(), imageStore.getImageList("minerFire"), resourceLimit,  actionPeriod(), animationPeriod(), 0);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(fireMiner);
        fireMiner.scheduleActions(scheduler, world, imageStore);
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        ActiveEntity miner = new MinerNotFull(id, getPosition(), images(), resourceLimit, 0, actionPeriod(), animationPeriod(), 0);

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
