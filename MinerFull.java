import java.util.List;
import java.util.Optional;
import processing.core.PImage;

final class MinerFull extends MovableEntity{

    private  int resourceLimit;

    public MinerFull(Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(),
                BlackSmith.class);

        if (fullTarget.isPresent() && moveTo(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, createAnimationAction(0),
                getAnimationPeriod());
    }

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        MinerNotFull miner = Loader.createMinerNotFull(resourceLimit,
                getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
        }
    }
}