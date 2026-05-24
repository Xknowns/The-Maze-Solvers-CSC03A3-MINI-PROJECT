package acsse.csc3b.graph;

import acsse.csc3b.datastructures.HashMap;
import acsse.csc3b.datastructures.LinkedList;
import acsse.csc3b.datastructures.Set;

/**
 * Graph ADT representing an image as a graph structure
 */
public class ImageGraph {
    private HashMap<Integer, GraphNode> nodes;
    private LinkedList<GraphEdge> edges;
    private int width;
    private int height;
    private String name;

    public ImageGraph(int width, int height, String name) {
        this.nodes = new HashMap<>();
        this.edges = new LinkedList<>();
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void addNode(GraphNode node) {
        nodes.put(node.getId(), node);
    }

    public GraphNode getNode(int id) {
        return nodes.get(id);
    }

    public GraphNode getNode(int x, int y) {
        return nodes.get(y * width + x);
    }

    public void addEdge(GraphNode from, GraphNode to, double weight) {
        GraphEdge edge = new GraphEdge(from, to, weight);
        edges.addLast(edge);
        from.addEdge(edge);
    }

    public void addUndirectedEdge(GraphNode from, GraphNode to, double weight) {
        addEdge(from, to, weight);
        addEdge(to, from, weight);
    }

    public LinkedList<GraphNode> getAllNodes() {
        LinkedList<GraphNode> result = new LinkedList<>();
        LinkedList<Integer> keys = nodes.keySet();
        Integer[] keyArray = keys.toArray(new Integer[0]);
        for (int i = 0; keyArray[i] != null; i++) {
            result.addLast(nodes.get(keyArray[i]));
        }
        return result;
    }

    public LinkedList<GraphEdge> getAllEdges() {
        return edges;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getName() { return name; }

    public void resetPathfinding() {
        LinkedList<GraphNode> allNodes = getAllNodes();
        GraphNode[] nodeArray = allNodes.toArray(new GraphNode[0]);
        for (int i = 0; nodeArray[i] != null; i++) {
            nodeArray[i].resetPathfinding();
        }
    }

    /**
     * Get graph statistics for display
     */
    public String getStatistics() {
        return String.format(
            "Graph: %s\nDimensions: %dx%d\nNodes: %d\nEdges: %d\nDensity: %.4f",
            name, width, height, getNodeCount(), getEdgeCount(),
            getNodeCount() > 0 ? (double) getEdgeCount() / (getNodeCount() * (getNodeCount() - 1)) : 0
        );
    }
}