package acsse.csc3b.similarity;

import acsse.csc3b.datastructures.HashMap;
import acsse.csc3b.datastructures.LinkedList;
import acsse.csc3b.graph.GraphEdge;
import acsse.csc3b.graph.GraphNode;
import acsse.csc3b.graph.ImageGraph;

/**
 * Provides algorithms for comparing graph similarity between maze structures.
 * Uses multiple structural and statistical metrics to compute an overall similarity score.
 *
 * @author The Maze Solvers
 * @version 1.0
 */
public class GraphSimilarity {
    
	 /**
     * Compares two graphs using multiple similarity metrics and returns a composite result.
     * Metrics include node count, edge count, degree distribution, and structural similarity.
     *
     * @param graph1 first graph to compare
     * @param graph2 second graph to compare
     * @return SimilarityResult containing score and detailed breakdown
     */
    public static SimilarityResult compare(ImageGraph graph1, ImageGraph graph2) {
        double nodeRatio = nodeCountSimilarity(graph1, graph2);
        double edgeRatio = edgeCountSimilarity(graph1, graph2);
        double degreeDist = degreeDistributionSimilarity(graph1, graph2);
        double structural = structuralSimilarity(graph1, graph2);
        
        // Weighted composite score
        double score = (nodeRatio * 0.2 + edgeRatio * 0.2 + degreeDist * 0.3 + structural * 0.3) * 100;
        
        String details = String.format(
            "Similarity Analysis:\n" +
            "===================\n" +
            "Node Count Ratio: %.2f%%\n" +
            "Edge Count Ratio: %.2f%%\n" +
            "Degree Distribution: %.2f%%\n" +
            "Structural Match: %.2f%%\n" +
            "-------------------\n" +
            "Overall Score: %.2f%%\n",
            nodeRatio * 100, edgeRatio * 100, degreeDist * 100, structural * 100, score
        );
        
        return new SimilarityResult(score, details);
    }
    
    /**
     * Computes similarity based on node count ratio between two graphs.
     *
     * @param g1 first graph
     * @param g2 second graph
     * @return similarity value between 0 and 1
     */
    private static double nodeCountSimilarity(ImageGraph g1, ImageGraph g2) {
        int n1 = g1.getNodeCount();
        int n2 = g2.getNodeCount();
        if (n1 == 0 && n2 == 0) return 1.0;
        if (n1 == 0 || n2 == 0) return 0.0;
        return Math.min(n1, n2) / (double) Math.max(n1, n2);
    }
    
    /**
     * Computes similarity based on edge count ratio between two graphs.
     *
     * @param g1 first graph
     * @param g2 second graph
     * @return similarity value between 0 and 1
     */
    private static double edgeCountSimilarity(ImageGraph g1, ImageGraph g2) {
        int e1 = g1.getEdgeCount();
        int e2 = g2.getEdgeCount();
        if (e1 == 0 && e2 == 0) return 1.0;
        if (e1 == 0 || e2 == 0) return 0.0;
        return Math.min(e1, e2) / (double) Math.max(e1, e2);
    }
    
    /**
     * Computes similarity between two graphs based on their degree distributions
     * using cosine similarity.
     *
     * @param g1 first graph
     * @param g2 second graph
     * @return similarity value between 0 and 1
     */
    private static double degreeDistributionSimilarity(ImageGraph g1, ImageGraph g2) {
        HashMap<Integer, Integer> deg1 = getDegreeDistribution(g1);
        HashMap<Integer, Integer> deg2 = getDegreeDistribution(g2);
        
        // Calculate cosine similarity between degree distributions
        double dotProduct = 0;
        double mag1 = 0;
        double mag2 = 0;
        
        LinkedList<Integer> allDegrees = new LinkedList<>();
        LinkedList<Integer> keys1 = deg1.keySet();
        Integer[] k1 = keys1.toArray(new Integer[0]);
        for (int i = 0; k1[i] != null; i++) {
            allDegrees.addLast(k1[i]);
        }
        
        LinkedList<Integer> keys2 = deg2.keySet();
        Integer[] k2 = keys2.toArray(new Integer[0]);
        for (int i = 0; k2[i] != null; i++) {
            if (!allDegrees.contains(k2[i])) {
                allDegrees.addLast(k2[i]);
            }
        }
        
        Integer[] allDegs = allDegrees.toArray(new Integer[0]);
        for (int i = 0; allDegs[i] != null; i++) {
            int d = allDegs[i];
            Integer v1 = deg1.get(d);
            Integer v2 = deg2.get(d);
            int val1 = v1 != null ? v1 : 0;
            int val2 = v2 != null ? v2 : 0;
            
            dotProduct += val1 * val2;
            mag1 += val1 * val1;
            mag2 += val2 * val2;
        }
        
        if (mag1 == 0 || mag2 == 0) return 0.0;
        return dotProduct / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }
    
    /**
     * Builds a degree distribution map for a graph.
     *
     * @param graph input graph
     * @return map of degree -> frequency
     */
    private static HashMap<Integer, Integer> getDegreeDistribution(ImageGraph graph) {
        HashMap<Integer, Integer> dist = new HashMap<>();
        LinkedList<GraphNode> nodes = graph.getAllNodes();
        GraphNode[] nodeArray = nodes.toArray(new GraphNode[0]);
        
        for (int i = 0; nodeArray[i] != null; i++) {
            int degree = 0;
            GraphEdge[] edges = nodeArray[i].getEdges().toArray(new GraphEdge[0]);
            for (int j = 0; edges[j] != null; j++) {
                degree++;
            }
            Integer count = dist.get(degree);
            dist.put(degree, count != null ? count + 1 : 1);
        }
        return dist;
    }
    
    /**
     * Computes structural similarity using clustering coefficient comparison.
     *
     * @param g1 first graph
     * @param g2 second graph
     * @return similarity value between 0 and 1
     */
    private static double structuralSimilarity(ImageGraph g1, ImageGraph g2) {
        // Simplified: compare average clustering-like measure
        double c1 = computeClusteringCoefficient(g1);
        double c2 = computeClusteringCoefficient(g2);
        
        if (c1 == 0 && c2 == 0) return 1.0;
        if (c1 == 0 || c2 == 0) return 0.0;
        return 1.0 - Math.abs(c1 - c2) / Math.max(c1, c2);
    }
    
    /**
     * Computes the average clustering coefficient of a graph.
     *
     * @param graph input graph
     * @return clustering coefficient value
     */
    private static double computeClusteringCoefficient(ImageGraph graph) {
        double totalClustering = 0;
        int validNodes = 0;
        
        LinkedList<GraphNode> nodes = graph.getAllNodes();
        GraphNode[] nodeArray = nodes.toArray(new GraphNode[0]);
        
        for (int i = 0; nodeArray[i] != null; i++) {
            GraphNode node = nodeArray[i];
            GraphEdge[] edges = node.getEdges().toArray(new GraphEdge[0]);
            
            // Count neighbors
            java.util.List<GraphNode> neighbors = new java.util.ArrayList<>();
            for (int j = 0; edges[j] != null; j++) {
                neighbors.add(edges[j].getTo());
            }
            
            int degree = neighbors.size();
            if (degree < 2) continue;
            
            // Count triangles
            int triangles = 0;
            for (int a = 0; a < degree; a++) {
                for (int b = a + 1; b < degree; b++) {
                    if (areConnected(neighbors.get(a), neighbors.get(b))) {
                        triangles++;
                    }
                }
            }
            
            int possible = degree * (degree - 1) / 2;
            if (possible > 0) {
                totalClustering += (double) triangles / possible;
                validNodes++;
            }
        }
        
        return validNodes > 0 ? totalClustering / validNodes : 0;
    }
    
    
    /**
     * Checks whether two nodes are directly connected by an edge.
     *
     * @param a first node
     * @param b second node
     * @return true if connected, false otherwise
     */
    private static boolean areConnected(GraphNode a, GraphNode b) {
        GraphEdge[] edges = a.getEdges().toArray(new GraphEdge[0]);
        for (int i = 0; edges[i] != null; i++) {
            if (edges[i].getTo().equals(b)) return true;
        }
        return false;
    }
}