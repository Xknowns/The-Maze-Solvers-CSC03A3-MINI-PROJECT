// File: src/acsse/csc3b/algorithms/PathResult.java
package acsse.csc3b.algorithms;

import acsse.csc3b.graph.GraphNode;
import java.util.List;

/**
 * Represents the result of a pathfinding operation.
 * 
 * 
 * This class stores:
 * 
 * The computed path as a list of graph nodes
 * The total traversal cost
 * A status or informational message
 * 
 * 
 * It is used by pathfinding algorithms such as A* and Dijkstra
 * to return navigation results to the application.
 * 
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class PathResult 
{
	/**
     * Stores the sequence of nodes representing the discovered path.
     */
    private List<GraphNode> path;
    
    /**
     * Stores the total traversal cost of the path.
     */
    private double cost;
    
    /**
     * Stores a descriptive result message.
     */
    private String message;

    /**
     * Constructs a new PathResult object.
     * 
     * @param path the list of nodes representing the discovered path
     * @param cost the total traversal cost
     * @param message the status or informational message
     */
    public PathResult(List<GraphNode> path, double cost, String message) 
    {
        this.path = path;
        this.cost = cost;
        this.message = message;
    }

    /**
     * Returns the discovered path.
     * 
     * @return the list of graph nodes representing the path
     */
    public List<GraphNode> getPath() 
    {
    	return path;
    }
    
    /**
     * Returns the total traversal cost.
     * 
     * @return the path cost
     */
    public double getCost() 
    { 
    	return cost; 
    }
    
    /**
     * Returns the result message.
     * 
     * @return the status message
     */
    public String getMessage() 
    { 
    	return message; 
    }
    
    /**
     * Determines whether a valid path exists.
     * 
     * @return true if a valid path exists, otherwise false
     */
    public boolean hasPath() 
    {
        return path != null && !path.isEmpty();
    }
    
    /**
     * Generates a formatted string representation of the path result.
     * 
     * 
     * The output includes:
     * 
     * Status message
     * Total traversal cost
     * Number of steps
     * Ordered path nodes
     * 
     * @return a formatted string describing the path result
     */
    public String getPathString() 
    {
        if (!hasPath()) return "No path found";
        StringBuilder sb = new StringBuilder();
        sb.append(message).append("\n");
        sb.append("Cost: ").append(String.format("%.2f", cost)).append("\n");
        sb.append("Steps: ").append(path.size() - 1).append("\n");
        sb.append("Path:\n");
        
        for (int i = 0; i < path.size(); i++) 
        {
            sb.append("  ").append(path.get(i));
            if (i < path.size() - 1) sb.append(" ->");
            sb.append("\n");
            if (i > 0 && i % 5 == 0) sb.append("\n");
        }
        return sb.toString();
    }
}