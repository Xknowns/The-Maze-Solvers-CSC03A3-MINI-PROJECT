// File: src/acsse/csc3b/algorithms/Dijkstra.java
package acsse.csc3b.algorithms;

import acsse.csc3b.datastructures.HashMap;
import acsse.csc3b.datastructures.PriorityQueue;
import acsse.csc3b.graph.GraphEdge;
import acsse.csc3b.graph.GraphNode;
import acsse.csc3b.graph.ImageGraph;

/**
 * Implements Dijkstra's shortest path algorithm for determining
 * the minimum cost path between two nodes in a graph.
 * 
 * 
 * The algorithm explores neighboring nodes by continuously selecting
 * the node with the smallest known distance from the starting point.
 * 
 * 
 * 
 * This implementation is used within the maze-solving system to
 * navigate graph-based maze structures efficiently.
 * 
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class Dijkstra 
{
    
	/**
     * Finds the shortest path between the specified start and end nodes
     * using Dijkstra's algorithm.
     * 
     * @param graph the graph containing the maze structure
     * @param start the starting node
     * @param end the destination node
     * @return a PathResult object containing the shortest path,
     *         total traversal cost, and result message
     */
    public static PathResult findShortestPath(ImageGraph graph, GraphNode start, GraphNode end) 
    {
        graph.resetPathfinding();
        
        PriorityQueue<GraphNode> queue = new PriorityQueue<>();
        HashMap<Integer, Double> distances = new HashMap<>();
        
        start.setDistance(0);
        distances.put(start.getId(), 0.0);
        queue.enqueue(start);
        
        while (!queue.isEmpty()) 
        {
            GraphNode current = queue.dequeue();
            
            if (current.equals(end)) 
            {
                break;
            }
            
            if (current.isVisited()) continue;
            current.setVisited(true);
            
            GraphEdge[] edges = current.getEdges().toArray(new GraphEdge[0]);
            for (int i = 0; edges[i] != null; i++) 
            {
                GraphEdge edge = edges[i];
                GraphNode neighbor = edge.getTo();
                
                if (neighbor.isVisited()) continue;
                
                double newDist = current.getDistance() + edge.getWeight();
                
                Double existingDist = distances.get(neighbor.getId());
                if (existingDist == null || newDist < existingDist) 
                {
                    distances.put(neighbor.getId(), newDist);
                    neighbor.setDistance(newDist);
                    neighbor.setParent(current);
                    queue.enqueue(neighbor);
                }
            }
        }
        
        //Reconstruct path
        java.util.List<GraphNode> path = new java.util.ArrayList<>();
        GraphNode current = end;
        
        if (end.getParent() == null && !end.equals(start)) 
        {
            return new PathResult(null, Double.MAX_VALUE, "No path found!");
        }
        
        while (current != null) 
        {
            path.add(0, current);
            current = current.getParent();
        }
        
        return new PathResult(path, end.getDistance(), "Path found using Dijkstra");
    }
}