import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
   private Point position;
   private List<PImage> images;
   private int imageIndex;

   public Entity(Point position, List<PImage> images)
   {
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

   protected List<PImage> images(){return images;}
   protected int imageIndex(){return imageIndex;}
   protected void setImageIndex(int index){imageIndex = index;}
   public Point getPosition() {return position;}
   public void setPosition(Point pos) {position = pos;}
   public PImage getCurrentImage()
   {
      return images.get(imageIndex);
   }
}
