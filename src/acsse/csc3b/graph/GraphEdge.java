package acsse.csc3b.graph;

/**
 * Represents an edge connecting two nodes in a graph.
 * Each edge has a source node, a destination node, and an associated weight.
 *
 * @author The Maze Solvers
 * @version 1.0
 */
public class GraphEdge {
    private GraphNode from;
    private GraphNode to;
    private double weight;

    /**
     * Constructs a new GraphEdge connecting two nodes with a specified weight.
     *
     * @param from   the source node
     * @param to     the destination node
     * @param weight the cost/weight of the edge
     */
    public GraphEdge(GraphNode from, GraphNode to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * Returns the source node of this edge.
     *
     * @return the starting GraphNode
     */
    public GraphNode getFrom() { return from; }
    
    /**
     * Returns the destination node of this edge.
     *
     * @return the ending GraphNode
     */
    public GraphNode getTo() { return to; }
    
    /**
     * Returns the weight of this edge.
     *
     * @return the edge weight as a double
     */
    public double getWeight() { return weight; }

    /**
     * Returns a string representation of the edge in the format:
     * Edge(from -> to, w=weight)
     *
     * @return formatted string describing the edge
     */
    @Override
    public String toString() {
        return String.format("Edge(%s -> %s, w=%.2f)", from, to, weight);
    }
}