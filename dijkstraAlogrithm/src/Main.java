import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.print("Enter start node: ");
        int startNode = in.nextInt();

        System.out.print("Enter end node: ");
        int endNode = in.nextInt();

        System.out.println("Calculated Route will be: " + startNode + " -> " + endNode);

        ArrayList<GraphElement> graph = createGraph();

        System.out.println("Fastest Route: " + computeFastestRoute(graph, startNode, endNode));
    }

    public static ArrayList<GraphElement> createGraph() {
        ArrayList<GraphElement> weightedGraph = new ArrayList<>();

        // creating this graph: https://hodakhaledyehya.files.wordpress.com/2015/06/graph1.gif
        weightedGraph.add(new GraphElement(1, 2, 2));
        weightedGraph.add(new GraphElement(1, 4, 1));
        weightedGraph.add(new GraphElement(2, 4, 3));
        weightedGraph.add(new GraphElement(2, 5, 10));
        weightedGraph.add(new GraphElement(3, 1, 4));
        weightedGraph.add(new GraphElement(3, 6, 5));
        weightedGraph.add(new GraphElement(4, 3, 2));
        weightedGraph.add(new GraphElement(4, 5, 2));
        weightedGraph.add(new GraphElement(4, 6, 8));
        weightedGraph.add(new GraphElement(4, 7, 4));
        weightedGraph.add(new GraphElement(5, 7, 6));
        weightedGraph.add(new GraphElement(7, 6, 1));

        return weightedGraph;
    }

    public static String computeFastestRoute(ArrayList<GraphElement> graph, int startNode, int endNode) {
        HashMap<Integer, Distance> distances = initializeDistances(graph, startNode);
        int currentNode = startNode;

        // Nodes for which the shortest route has already been found are not considered further
        ArrayList<Integer> usedNodes = new ArrayList<>();

        while(currentNode != endNode) {
            ArrayList<GraphElement> graphElementsWithStartNode = getGraphElementsWithStartNode(graph, currentNode);
            int iterationCurrentNode = currentNode;

            // error handling for dead end nodes
            boolean noPossibleWayFromCurrentNode = (graphElementsWithStartNode.size() == 0);
            boolean noPossibleWayFromAnotherNode = distances.entrySet().stream()
                    .noneMatch(dist -> (dist.getValue().getDistance() > 0) && (!usedNodes.contains(dist.getKey())) && (dist.getKey() != iterationCurrentNode));

            if(noPossibleWayFromCurrentNode && noPossibleWayFromAnotherNode) {
                return "No possible Route found (Dead End)";
            } else if (noPossibleWayFromCurrentNode) {
                // change current node, because there are no available paths
                usedNodes.add(currentNode);
                currentNode = distances.entrySet().stream()
                        .filter(dist -> dist.getValue().getDistance() > 0 && !usedNodes.contains((dist.getKey())))
                        .collect(Collectors.toCollection(ArrayList::new))
                        .get(1)
                        .getKey();
                continue;
            }

            //iterate through all available paths from current node
            for (GraphElement graphElement : graphElementsWithStartNode) {
                int distanceFromEndNodeToStartNode = distances.get(graphElement.getEndNode()).getDistance();
                int distanceFromCurrentNodeToStartNode = distances.get(iterationCurrentNode).getDistance();
                int weightOfGraphElement = graphElement.getWeight();

                // check whether the distance entries can be optimized
                if (distanceFromEndNodeToStartNode > (distanceFromCurrentNodeToStartNode + weightOfGraphElement) || distanceFromEndNodeToStartNode == 0) {
                    //update the distance and the previous node
                    distances.get(graphElement.getEndNode()).setDistanceAndPreviousNode((distanceFromCurrentNodeToStartNode + weightOfGraphElement), currentNode);
                }
            }

            usedNodes.add(currentNode);

            // set new current node
            Optional <Map.Entry<Integer, Distance>> newCurrentNode = distances.entrySet().stream()
                    .filter(dist -> dist.getValue().getDistance() > 0 && !usedNodes.contains(dist.getKey()))
                    .min(Map.Entry.comparingByValue(Distance::compareTo));

            if(newCurrentNode.isPresent()) {
                currentNode = newCurrentNode.get().getKey();
            }
        }

        return constructShortestRoute(distances, startNode, endNode);
    }

    public static HashMap<Integer, Distance> initializeDistances(ArrayList<GraphElement> graph, int startNode) {
        HashMap<Integer, Distance> distances = new HashMap<>();
        graph.stream().map(GraphElement::getEndNode).distinct().forEach(endNode -> distances.put(endNode, new Distance(0, 0)));
        distances.put(startNode, new Distance(0, 0));

        return distances;
    }

    public static ArrayList<GraphElement> getGraphElementsWithStartNode(ArrayList<GraphElement> graph, int currentNode) {
        return graph.stream()
                .filter(grEl -> grEl.getStartNode() == currentNode)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String constructShortestRoute(HashMap<Integer, Distance> distances, int startNode, int endNode) {
        if(distances.get(endNode).getDistance() == 0) return "No possible Route found";

        ArrayList<Integer> usedNodes = new ArrayList<>();
        int currentEndNode = endNode;

        while(currentEndNode != startNode) {
            usedNodes.add(currentEndNode);
            Distance distanceFromEndNodeToStartNode = distances.get(currentEndNode);
            currentEndNode = distanceFromEndNodeToStartNode.getPreviousNode();
        }

        usedNodes.add(currentEndNode);

        return reverseList(usedNodes).toString() + " (Length: " + distances.get(endNode).getDistance() + ")";
    }

    public static ArrayList<Integer> reverseList(ArrayList<Integer> originalList) {
        if (originalList.size() > 1) {
            int val = originalList.remove(0);
            reverseList(originalList);
            originalList.add(val);
        }

        return originalList;
    }
}
