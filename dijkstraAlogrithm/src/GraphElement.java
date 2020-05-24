public class GraphElement {

    private final int startNode;
    private final int endNode;
    private final int weight;

    public GraphElement(int startNode, int endNode, int weight) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = weight;
    }

    public int getStartNode() {
        return startNode;
    }

    public int getEndNode() {
        return endNode;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "" + startNode + " --(" + weight + ")--> " + endNode;
    }
}
