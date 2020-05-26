import java.util.Objects;

public class Distance implements Comparable<Distance> {

    private int distance;
    private int previousNode;

    public Distance( int distance, int previousNode) {
        this.distance = distance;
        this.previousNode = previousNode;
    }

    public int getDistance() {
        return distance;
    }

    public int getPreviousNode() {
        return previousNode;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setPreviousNode(int previousNode) {
        this.previousNode = previousNode;
    }

    public void setDistanceAndPreviousNode (int distance, int previousNode) {
        this.distance = distance;
        this.previousNode = previousNode;
    }

    @Override
    public String toString() {
        return "Distance {" +
                " distance = " + distance +
                ", previousNode = " + previousNode +
                '}';
    }

    @Override
    public int compareTo(Distance distance) {
        return Integer.compare(this.distance, distance.getDistance());
    }

}
