// File: src/acsse/csc3b/algorithms/AStar.java
package acsse.csc3b.algorithms;

import acsse.csc3b.datastructures.HashMap;
import acsse.csc3b.datastructures.PriorityQueue;
import acsse.csc3b.graph.GraphEdge;
import acsse.csc3b.graph.GraphNode;
import acsse.csc3b.graph.ImageGraph;

/**
 * Implements the A* (A-Star) pathfinding algorithm for finding
 * the shortest path between two nodes in a graph.
 * 
 * 
 * The algorithm combines the actual distance travelled from the
 * start node with a heuristic estimate to determine the most
 * efficient path to the destination.
 * 
 * 
 * 
 * This implementation is used in the maze solver system where
 * maze images are converted into graph structures and traversed
 * to determine the optimal route.
 * 
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class AStar {
    
	 /**
     * Finds the shortest path between the start and end nodes
     * using the A* pathfinding algorithm.
     * 
     * @param graph the graph containing maze nodes and edges
     * @param start the starting node
     * @param end the destination node
     * @return a PathResult object containing the discovered path,
     *         total cost, and status message
     */
    public static PathResult findShortestPath(ImageGraph graph, GraphNode start, GraphNode end) {
        graph.resetPathfinding();
        
        PriorityQueue<GraphNode> openSet = new PriorityQueue<>();
        HashMap<Integer, Double> gScore = new HashMap<>();
        
        start.setDistance(0);
        start.setHeuristic(heuristic(start, end));
        gScore.put(start.getId(), 0.0);
        openSet.enqueue(start);
        
        while (!openSet.isEmpty()) {
            GraphNode current = openSet.dequeue();
            
            if (current.equals(end)) {
                break;
            }
            
            if (current.isVisited()) continue;
            current.setVisited(true);
            
            GraphEdge[] edges = current.getEdges().toArray(new GraphEdge[0]);
            for (int i = 0; edges[i] != null; i++) {
                GraphEdge edge = edges[i];
                GraphNode neighbor = edge.getTo();
                
                if (neighbor.isVisited()) continue;
                
                double tentativeG = current.getDistance() + edge.getWeight();
                
                Double existingG = gScore.get(neighbor.getId());
                if (existingG == null || tentativeG < existingG) {
                    gScore.put(neighbor.getId(), tentativeG);
                    neighbor.setParent(current);
                    neighbor.setDistance(tentativeG);
                    neighbor.setHeuristic(heuristic(neighbor, end));
                    openSet.enqueue(neighbor);
                }
            }
        }
        
        //Reconstruct path
        java.util.List<GraphNode> path = new java.util.ArrayList<>();
        GraphNode current = end;
        
        if (end.getParent() == null && !end.equals(start)) {
            return new PathResult(null, Double.MAX_VALUE, "No path found!");
        }
        
        while (current != null) {
            path.add(0, current);
            current = current.getParent();
        }
        
        return new PathResult(path, end.getDistance(), "Path found using A*");
    }
    
    /**
     * Calculates the Euclidean distance heuristic between two nodes.
     * 
     * 
     * This heuristic estimates the direct distance between nodes
     * and helps guide the A* algorithm toward the destination more efficiently.
     * 
     * 
     * @param a the first graph node
     * @param b the second graph node
     * @return the Euclidean distance between the two nodes
     */
    private static double heuristic(GraphNode a, GraphNode b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}