import java.util.Objects;

public class Distance {

    private int startNode;
    private int endNode;
    private int distance;
    private int previousNode;

    public Distance(int startNode, int endNode, int distance, int previousNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.distance = distance;
        this.previousNode = previousNode;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public int getDistance() {
        return distance;
    }

    public int getPreviousNode() {
        return previousNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(int endNode) {
        this.endNode = endNode;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPreviousNode(int previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public String toString() {
        return "Distance {" +
                "startNode = " + startNode +
                ", endNode = " + endNode +
                ", distance = " + distance +
                ", previousNode = " + previousNode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return startNode == distance1.startNode &&
                endNode == distance1.endNode &&
                distance == distance1.distance &&
                previousNode == distance1.previousNode;
    }
}
