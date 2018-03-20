import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class MoveableEntity extends AnimatedEntity
{

    PathingStrategy aStarStrategy = new AStarPathingStrategy();
    public MoveableEntity(Point position, List<PImage> images, int actionPeriod, int animationPeriod, int repeatCount) {
        super(position, images, actionPeriod, animationPeriod, repeatCount);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
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

    public Point nextPosition(WorldModel world, Point destPos) {
        List<Point> points;
        List<Point> path = new ArrayList<>();
        Point pos = getPosition();

        points = aStarStrategy.computePath(pos, destPos,
                p ->  world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        //DIAGONAL_NEIGHBORS);
        //DIAGONAL_CARDINAL_NEIGHBORS)

        if (points.size() == 0) {
            System.out.println("No path found");
            return getPosition();
        }

        pos = points.get(0);
        path.add(pos);

        return path.get(0);
    }

    private static boolean neighbors(Point p1, Point p2) {
        return p1.getX() + 1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() - 1 == p2.getX() && p1.getY() == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY() + 1 == p2.getY() ||
                p1.getX() == p2.getX() && p1.getY() - 1 == p2.getY();
    }


    private static final Function<Point, Stream<Point>> DIAGONAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX() - 1, point.getY() - 1))
                            .add(new Point(point.getX() + 1, point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY() + 1))
                            .add(new Point(point.getX() + 1, point.getY() - 1))
                            .build();


    private static final Function<Point, Stream<Point>> DIAGONAL_CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX() - 1, point.getY() - 1))
                            .add(new Point(point.getX() + 1, point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY() + 1))
                            .add(new Point(point.getX() + 1, point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();

}
