package acsse.csc3b.graph;

import acsse.csc3b.datastructures.LinkedList;

/**
 * Represents a node in the graph, typically corresponding to a pixel or region
 * in an image used for maze or pathfinding representation.
 * Each node stores position, color data, connectivity (edges), and pathfinding metadata.
 *
 * @author The Maze Solvers
 * @version 1.0
 */
public class GraphNode implements Comparable<GraphNode> {
    private int id;
    private int x;
    private int y;
    private int r, g, b;
    private boolean isWall; // True if this node represents a wall/obstacle
    private LinkedList<GraphEdge> edges;
    
    // For pathfinding
    private double distance = Double.MAX_VALUE;
    private double heuristic = 0;
    private GraphNode parent = null;
    private boolean visited = false;

    /**
     * Constructs a GraphNode with position, ID, and RGB color values.
     *
     * @param id unique identifier of the node
     * @param x x-coordinate
     * @param y y-coordinate
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     */
    public GraphNode(int id, int x, int y, int r, int g, int b) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.isWall = false;
        this.edges = new LinkedList<>();
    }

    /**
     * Adds an edge to this node's adjacency list.
     *
     * @param edge the GraphEdge to add
     */
    public void addEdge(GraphEdge edge) {
        edges.addLast(edge);
    }

    /**
     * Returns all edges connected to this node.
     *
     * @return linked list of GraphEdge objects
     */
    public LinkedList<GraphEdge> getEdges() {
        return edges;
    }

    // Getters and setters
    /** @return node ID */
    public int getId() { return id; }
    
    /** @return x-coordinate */
    public int getX() { return x; }
    
    /** @return y-coordinate */
    public int getY() { return y; }
    
    /** @return red component */
    public int getR() { return r; }
    
    /** @return green component */
    public int getG() { return g; }
    
    /** @return blue component */
    public int getB() { return b; }
    
    /** @return true if this node is a wall/obstacle */
    public boolean isWall() { return isWall; }
    
    /**
     * Sets whether this node is a wall.
     *
     * @param wall true if node is a wall
     */
    public void setWall(boolean wall) { this.isWall = wall; }
    
    /** @return distance from start node */
    public double getDistance() { return distance; }
    
    /**
     * Sets the distance from the start node.
     *
     * @param distance g-cost value
     */
    public void setDistance(double distance) { this.distance = distance; }
    
    /** @return heuristic value to goal */
    public double getHeuristic() { return heuristic; }
    /**
     * Sets heuristic value.
     *
     * @param heuristic estimated cost to goal
     */
    public void setHeuristic(double heuristic) { this.heuristic = heuristic; }
    
    /** @return f-score (distance + heuristic) */
    public double getFScore() { return distance + heuristic; }
    
    /** @return parent node in path */
    public GraphNode getParent() { return parent; }
    /**
     * Sets the parent node in the path.
     *
     * @param parent previous node in path
     */
    public void setParent(GraphNode parent) { this.parent = parent; }
    
    /** @return true if node has been visited */
    public boolean isVisited() { return visited; }
    /**
     * Marks node as visited or unvisited.
     *
     * @param visited visitation state
     */
    public void setVisited(boolean visited) { this.visited = visited; }
    
    /**
     * Resets pathfinding values for this node.
     */
    public void resetPathfinding() {
        distance = Double.MAX_VALUE;
        heuristic = 0;
        parent = null;
        visited = false;
    }

    /**
     * Compares nodes based on f-score for priority queue ordering.
     *
     * @param other another GraphNode
     * @return comparison result
     */
    @Override
    public int compareTo(GraphNode other) {
        return Double.compare(this.getFScore(), other.getFScore());
    }

    /**
     * Checks equality based on node ID.
     *
     * @param obj object to compare
     * @return true if IDs match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GraphNode other = (GraphNode) obj;
        return id == other.id;
    }

    /**
     * Returns hash code based on node ID.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns a string representation of the node.
     *
     * @return formatted string "Node[id](x,y)"
     */
    @Override
    public String toString() {
        return String.format("Node[%d](%d,%d)", id, x, y);
    }
}