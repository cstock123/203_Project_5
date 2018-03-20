import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity
{
    private int animationPeriod;
    private int repeatCount;

    public AnimatedEntity(Point position, List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        this.repeatCount = repeatCount;
    }

    protected int animationPeriod(){return animationPeriod;}

    public void nextImage()
    {
        setImageIndex((imageIndex() + 1) % images().size());
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this,
                new Animation(this, repeatCount),
                animationPeriod);
    }

    public int getAnimationPeriod(){return animationPeriod;}
}
