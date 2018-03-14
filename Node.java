public class Node {
    private int g, h, f;
    private Node previous;
    private Point current;

    public Node(int g, int h, int f, Point current, Node previous) {
        this.g = g;
        this.h = h;
        this.f = f;
        this.current = current;
        this.previous = previous;
    }

    public Point current() { return current; }
    public int getF() { return f; }
    public Node previous() { return previous; }

    public boolean equals(Object o){
        if(o == null) {
            return false;
        }
        if(o.getClass() != getClass()) {
            return false;
        }
        Node n = (Node)o;
        return current.equals(n.current);
    }

    public String toString() {
        return current.getX() + " " + current.getY();
    }
}
