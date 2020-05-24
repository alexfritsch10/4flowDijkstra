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

        ArrayList<GraphElement> weightedGraph = createGraph();

        System.out.println("Fastest Route: " + computeFastestRoute(weightedGraph, startNode, endNode));
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
        ArrayList<Distance> distances = initializeDistances(graph, startNode);
        int currentNode = startNode;

        // Nodes for which the shortest route has already been found are not considered further
        ArrayList<Integer> usedNodes = new ArrayList<>();

        while(currentNode != endNode) {
            ArrayList<GraphElement> graphElementsWithStartNode = getGraphElementsWithStartNode(graph, startNode, currentNode);
            int finalCurrentNode = currentNode;

            // error handling for dead end nodes
            boolean noPossibleWayFromCurrentNode = (graphElementsWithStartNode.size() == 0);
            boolean noPossibleWayFromAnotherNode = distances.stream().noneMatch(a -> (a.getDistance() > 0) && (!usedNodes.contains(a.getEndNode())) && (a.getEndNode() != finalCurrentNode));

            if(noPossibleWayFromCurrentNode && noPossibleWayFromAnotherNode) {
                return "No possible Route found (Dead End)";
            } else if (noPossibleWayFromCurrentNode) {
                // change current node, because there are no available paths
                usedNodes.add(currentNode);
                currentNode = distances.stream().filter(a -> a.getDistance() > 0 && !usedNodes.contains((a.getEndNode()))).collect(Collectors.toCollection(ArrayList::new)).get(1).getEndNode();
                continue;
            }

            //iterate through all available paths from current node
            for (int i = 0; i < graphElementsWithStartNode.size(); i++) {
                int finalI = i;
                Optional<Distance> actualDistanceFromEndNodeToStartNode = distances.stream().filter(a -> a.getEndNode() == graphElementsWithStartNode.get(finalI).getEndNode()).findFirst();
                Optional<Distance> actualDistanceFromCurrentNodeToStartNode = distances.stream().filter(a -> a.getEndNode() == finalCurrentNode).findFirst();
                int weightOfGraphElement = graphElementsWithStartNode.get(i).getWeight();

                if(actualDistanceFromCurrentNodeToStartNode.isPresent() && actualDistanceFromEndNodeToStartNode.isPresent()) {
                    int distanceFromCurrentNodeToStartNode = actualDistanceFromCurrentNodeToStartNode.get().getDistance();
                    int distanceFromEndNodeToStartNode = actualDistanceFromEndNodeToStartNode.get().getDistance();


                    // check whether the distance entries can be optimized
                    if (distanceFromEndNodeToStartNode > (distanceFromCurrentNodeToStartNode + weightOfGraphElement) || distanceFromEndNodeToStartNode == 0) {
                        Optional<Distance> mutateDistance = distances.stream().filter(a -> a.getEndNode() == graphElementsWithStartNode.get(finalI).getEndNode()).findFirst();

                        // update the distance from the respective node to the start node
                        if (mutateDistance.isPresent()) {
                            Distance extractedDistance = distances.remove(distances.indexOf(mutateDistance.get()));
                            extractedDistance.setDistance(distanceFromCurrentNodeToStartNode + weightOfGraphElement);
                            extractedDistance.setPreviousNode(currentNode);
                            distances.add(extractedDistance);
                        }
                    }
                }
            }

            usedNodes.add(currentNode);

            // set new current node
            Optional<Distance> newCurrent = distances.stream().filter(a -> a.getDistance() > 0 && !usedNodes.contains(a.getEndNode())).min(Comparator.comparingInt(Distance::getDistance));

            if(newCurrent.isPresent()) {
                currentNode = newCurrent.get().getEndNode();
            }
        }

        return constructShortestRoute(distances, startNode, endNode);
    }

    public static ArrayList<Distance> initializeDistances(ArrayList<GraphElement> graph, int startNode) {
        ArrayList<Distance> distances = new ArrayList<>();
        graph.stream().map(GraphElement::getEndNode).distinct().forEach(a -> distances.add(new Distance(startNode, a, 0, 0)));
        distances.add(new Distance(startNode, startNode, 0, 0));

        return distances;
    }

    public static ArrayList<GraphElement> getGraphElementsWithStartNode(ArrayList<GraphElement> graph, int startNode, int currentNode) {
        return graph.stream().filter(a -> a.getStartNode() == currentNode && a.getEndNode() != startNode).collect(Collectors.toCollection(ArrayList::new));
    }

    public static String constructShortestRoute(ArrayList<Distance> distances, int startNode, int endNode) {
        ArrayList<Integer> usedNodes = new ArrayList<>();
        boolean nodeIsStartNode = false;
        int currentEndNode = endNode;
        int totalLength = 0;

        while(currentEndNode != startNode) {
            int finalCurrentEndNode = currentEndNode;
            usedNodes.add(currentEndNode);
            Optional<Distance> distanceFromEndNodeToStartNode = distances.stream().filter(a -> a.getEndNode() == finalCurrentEndNode).findFirst();

            if(distanceFromEndNodeToStartNode.isEmpty() || distanceFromEndNodeToStartNode.get().getDistance() == 0) {
                return "No possible Route found";
            }

            if(currentEndNode == endNode) {
                totalLength = distanceFromEndNodeToStartNode.get().getDistance();

            }

            currentEndNode = distanceFromEndNodeToStartNode.get().getPreviousNode();

        }

        usedNodes.add(currentEndNode);

        return reverseList(usedNodes).toString() + " (Length: " + totalLength + ")";
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
