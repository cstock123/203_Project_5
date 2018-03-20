import processing.core.PImage;

import java.util.List;
import java.util.Random;

public abstract class ActiveEntity extends Entity
{
    private int actionPeriod;
    private static final Random rand = new Random();


    public ActiveEntity(Point position, List<PImage> images, int actionPeriod)
    {
        super(position, images);
        this.actionPeriod = actionPeriod;
    }

    protected int actionPeriod(){return actionPeriod;}
    protected Random rand(){return rand;}

    abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore),
                actionPeriod);
    }
}
