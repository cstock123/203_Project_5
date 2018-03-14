import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity {

    private int actionPeriod;

    public ActivityEntity(Point position, List<PImage> images, int actionPeriod) {
        super(position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() { return actionPeriod; }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
