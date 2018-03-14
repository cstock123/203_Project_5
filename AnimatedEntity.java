import processing.core.PImage;
import java.util.List;

public abstract class AnimatedEntity extends ActivityEntity{

    private int animationPeriod;
    private int imageIndex;

    public AnimatedEntity(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        imageIndex = 0;
    }

    public void setImageIndex(int index) { imageIndex = index; }
    public int getAnimationPeriod() { return animationPeriod; }
    public void nextImage() { setImageIndex((imageIndex + 1) % getImages().size()); }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
}