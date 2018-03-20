public class Node {

    private Point pos;

    private int gScore;

    private int hScore;

    private Node parent;

    public Node(Point pos, int gScore, int hScore, Node parent){
        this.pos = pos;
        this.gScore = gScore;
        this.hScore = hScore;
        this.parent = parent;
    }

    public Point getPos(){return pos;}

    public int getgScore(){return gScore;}

    public Node getParent(){return parent;}

    public int getHur(){return gScore + hScore;}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node aStarNode = (Node) o;

        if (Double.compare(aStarNode.gScore, gScore) != 0) return false;
        if (Double.compare(aStarNode.hScore, hScore) != 0) return false;
        return pos != null ? pos.equals(aStarNode.pos) : aStarNode.pos == null;
    }

    public int hashCode() {
        int result;
        long temp;
        result = pos != null ? pos.hashCode() : 0;
        temp = Double.doubleToLongBits(gScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(hScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

}
