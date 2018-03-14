import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
        imageIndex = 0;
    }

    public List<PImage> getImages() { return images; }
    public PImage getCurrentImage() {
        return (images.get(imageIndex));
    }
    public Point getPosition() { return position; }
    public void setPosition(Point pos) { position = pos; }
}
