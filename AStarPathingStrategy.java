import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        Comparator<Node> comp1 = (star1, star2) -> (star1.getHur()) - (star2.getHur());
        PriorityQueue<Node> openQue = new PriorityQueue<>(comp1);
        Map<Point, Node> openSet = new HashMap<>();
        Map<Point, Node> closedSet = new HashMap<>();
        Node first = new Node(start, 0, distance(start, end), null);
        openQue.add(first);
        openSet.put(first.getPos(), first);

        while(openQue.size() != 0){
            Node current = openQue.poll();
            openSet.put(current.getPos(), current);
            closedSet.put(current.getPos(), current);

            if (withinReach.test(current.getPos(), end)) { return construct_path(closedSet, current); }

            List<Point> neighbors = potentialNeighbors.apply(current.getPos()).collect(Collectors.toList());

            for (Point test:neighbors){

                Node node1 = new Node(test, current.getgScore() + 1, distance(test, end), current);

                if (!canPassThrough.test(test)) { continue; }

                if (closedSet.containsKey(test)) { continue; }

                if (!openSet.containsKey(node1.getPos())) { openQue.add(node1); }

            }
            openSet.remove(current.getPos());
            openQue.remove(current);
        }
        return new ArrayList<>();
    }

    public List<Point> construct_path(Map<Point, Node> closed, Node cur)
    {
        List<Point> total_path = new ArrayList<>();
        total_path.add(cur.getPos());
        Node next = closed.get(cur.getParent().getPos());
        while(next != null){
            if (next.getParent() == null){break;}
            total_path.add(0, next.getPos());
            next = closed.get(next.getParent().getPos());
        }
        return total_path;
    }

    private int distance(Point pt1, Point pt2)
    {
        return Math.abs((pt2.x-pt1.x)) + Math.abs((pt2.y - pt1.y));
    }
}
