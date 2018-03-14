import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Node> openList = new ArrayList<>();
        List<Node> closeList = new ArrayList<>();
        int hval = DistanceFrom(start, end);
        int fval = hval;
        Node current = new Node(0, hval, fval, start, null);

        Predicate<Point> notOnOpenList = p -> {
            for (Node n : openList) {
                if (n.current().equals(p)) {
                    return false;
                }
            }
            return true;
        };

        Predicate<Point> notOnCloseList = p -> {
            for (Node n : closeList) {
                if (n.current().equals(p)) {
                    return false;
                }
            }
            return true;
        };

        //filter out into a list of points the potential neighbors that are not on the open or closed list
        //and can be passed through and are not the start or end points.

        while (!withinReach.test(current.current(), end)) {
            List<Point> potentialNext = potentialNeighbors.apply(current.current())
                    .filter(canPassThrough)
                    .filter(notOnCloseList)
                    .filter(notOnOpenList)
                    .filter(pt -> !pt.equals(start) && !pt.equals(end))
                    .collect(Collectors.toList());


            //calculate the distances and add to the open list and nodeList
            int g, h, f;
            for (Point p : potentialNext) {
                g = DistanceFrom(p, start);
                h = DistanceFrom(p, end);
                f = g + h;
                openList.add(new Node(g, h, f, p, current));
            }
            closeList.add(current);
            openList.remove(current);
            if(openList.size() > 0) {
                current = openList.get(smallestF(openList));
            } else {
                return buildPath(current);
            }
        }
        System.out.println(current);
        return buildPath(current);
    }

    //recursive function that builds the path until it reaches the null previous node.
    public List<Point> buildPath(Node current) {
        List<Point> path = new ArrayList<>();
        path.add(0, current.current());
        while(current.previous() != null) {
            path.add(0, current.previous().current());
            current = current.previous();
        }
        path.remove(0);
        return path;
    }

    public int DistanceFrom(Point p,  Point goal) {
        return Math.abs(p.getX() - goal.getX()) + Math.abs(p.getY() - goal.getY());
    }

    public int smallestF(List<Node> openList) {

        //System.out.println(openList.size());
        Node smallest = openList.get(0);
        for(Node n : openList) {
            if(n.getF() < smallest.getF()) {
                smallest = n;
            }
        }
        return openList.indexOf(smallest);
    }
}
