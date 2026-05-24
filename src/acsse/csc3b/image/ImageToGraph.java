package acsse.csc3b.image;

import acsse.csc3b.graph.GraphNode;
import acsse.csc3b.graph.ImageGraph;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Utility class responsible for converting images into graph representations.
 * Supports both maze conversion (binary wall/path graphs) and Region Adjacency Graph (RAG) creation.
 *
 * @author The Maze Solvers
 */
public class ImageToGraph {
    
	/**
     * Converts a maze image into a graph representation.
     * White/light pixels are treated as paths, while dark pixels are treated as walls.
     * The image may be downsampled for performance reasons.
     *
     * @param image the input maze image
     * @param name the name assigned to the resulting graph
     * @return an ImageGraph representing the maze structure
     */
    public static ImageGraph convertMaze(Image image, String name) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        
        // Downsample large images for performance
        int scale = 1;
        while (width / scale > 100 || height / scale > 100) {
            scale++;
        }
        
        int graphWidth = width / scale;
        int graphHeight = height / scale;
        
        ImageGraph graph = new ImageGraph(graphWidth, graphHeight, name);
        PixelReader reader = image.getPixelReader();
        
        // Create nodes for all pixels first
        for (int y = 0; y < graphHeight; y++) {
            for (int x = 0; x < graphWidth; x++) {
                int imgX = x * scale;
                int imgY = y * scale;
                
                Color color = reader.getColor(imgX, imgY);
                int r = (int) (color.getRed() * 255);
                int g = (int) (color.getGreen() * 255);
                int b = (int) (color.getBlue() * 255);
                
                // Determine if this is a path (light) or wall (dark)
                double brightness = (r + g + b) / 3.0;
                boolean isWall = brightness <= 128;
                
                int id = y * graphWidth + x;
                GraphNode node = new GraphNode(id, x, y, r, g, b);
                node.setWall(isWall);
                graph.addNode(node);
            }
        }
        
        // Create edges between adjacent path nodes (4-connectivity)
        for (int y = 0; y < graphHeight; y++) {
            for (int x = 0; x < graphWidth; x++) {
                GraphNode node = graph.getNode(x, y);
                if (node.isWall()) continue; // Skip walls
                
                // Check 4 neighbors
                int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    if (nx >= 0 && nx < graphWidth && ny >= 0 && ny < graphHeight) {
                        GraphNode neighbor = graph.getNode(nx, ny);
                        if (!neighbor.isWall()) {
                            double weight = 1.0; // Uniform weight for maze
                            graph.addUndirectedEdge(node, neighbor, weight);
                        }
                    }
                }
            }
        }
        
        return graph;
    }

    /**
     * Converts an image into a Region Adjacency Graph (RAG).
     * Each region represents a block of pixels with an average color.
     *
     * @param image the input image
     * @param name the name of the graph
     * @param regionSize the size of each region block
     * @return an ImageGraph representing region adjacency
     */
    public static ImageGraph convertToRAG(Image image, String name, int regionSize) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        
        int regionsX = Math.max(1, width / regionSize);
        int regionsY = Math.max(1, height / regionSize);
        
        ImageGraph graph = new ImageGraph(regionsX, regionsY, name);
        PixelReader reader = image.getPixelReader();
        
        // Create region nodes with average color
        for (int ry = 0; ry < regionsY; ry++) {
            for (int rx = 0; rx < regionsX; rx++) {
                int startX = rx * regionSize;
                int startY = ry * regionSize;
                int endX = Math.min(startX + regionSize, width);
                int endY = Math.min(startY + regionSize, height);
                
                long sumR = 0, sumG = 0, sumB = 0;
                int count = 0;
                
                for (int y = startY; y < endY; y++) {
                    for (int x = startX; x < endX; x++) {
                        Color color = reader.getColor(x, y);
                        sumR += color.getRed() * 255;
                        sumG += color.getGreen() * 255;
                        sumB += color.getBlue() * 255;
                        count++;
                    }
                }
                
                int avgR = (int) (sumR / count);
                int avgG = (int) (sumG / count);
                int avgB = (int) (sumB / count);
                
                int id = ry * regionsX + rx;
                GraphNode node = new GraphNode(id, rx, ry, avgR, avgG, avgB);
                graph.addNode(node);
            }
        }
        
        // Connect adjacent regions
        for (int ry = 0; ry < regionsY; ry++) {
            for (int rx = 0; rx < regionsX; rx++) {
                GraphNode node = graph.getNode(rx, ry);
                
                int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
                for (int[] dir : directions) {
                    int nx = rx + dir[0];
                    int ny = ry + dir[1];
                    
                    if (nx >= 0 && nx < regionsX && ny >= 0 && ny < regionsY) {
                        GraphNode neighbor = graph.getNode(nx, ny);
                        double weight = colorDistance(node, neighbor);
                        graph.addUndirectedEdge(node, neighbor, weight);
                    }
                }
            }
        }
        
        return graph;
    }

    /**
     * Calculates the Euclidean distance between two nodes based on RGB color values.
     *
     * @param a first graph node
     * @param b second graph node
     * @return color distance as a double value
     */
    private static double colorDistance(GraphNode a, GraphNode b) {
        double dr = a.getR() - b.getR();
        double dg = a.getG() - b.getG();
        double db = a.getB() - b.getB();
        return Math.sqrt(dr * dr + dg * dg + db * db);
    }
}