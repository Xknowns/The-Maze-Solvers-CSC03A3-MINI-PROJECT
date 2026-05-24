package acsse.csc3b.client;

import acsse.csc3b.algorithms.AStar;
import acsse.csc3b.algorithms.Dijkstra;
import acsse.csc3b.algorithms.PathResult;
import acsse.csc3b.datastructures.LinkedList;
import acsse.csc3b.graph.GraphEdge;
import acsse.csc3b.graph.GraphNode;
import acsse.csc3b.graph.ImageGraph;
import acsse.csc3b.image.ImageToGraph;
import acsse.csc3b.similarity.GraphSimilarity;
import acsse.csc3b.similarity.SimilarityResult;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Main graphical user interface for the Maze Solver application.
 * 
 * This class manages all user interaction and visualization features
 * of the system. It allows users to:
 *Upload maze images
 *Convert images into graph structures
 *Select start and end nodes
 *Solve mazes using graph algorithms
 *Compare graph similarities between images
 *Visualize graph nodes and traversal paths
 * 
 * The interface uses JavaFX components such as:
 *Buttons
 *Image views
 *Text areas
 *Labels
 *Overlay graphics
 * 
 * This class acts as the central controller connecting:
 * Graph generation
 * Pathfinding algorithms
 * Similarity analysis
 * User interaction
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class UserPane extends BorderPane {

    

    private ImageGraph mazeGraph = null;
    private ImageGraph comparisonGraph = null;
    private GraphNode startNode = null;
    private GraphNode endNode = null;
    private Image mazeImage = null;
    private Image comparisonImage = null;

    // Track the downsample scale used by ImageToGraph
    private int downsampleScale = 1;
    private int graphWidth = 0;
    private int graphHeight = 0;

    private ImageView imageView;
    private ImageView imageView2;
    private StackPane mazeContainer;
    private StackPane compareContainer;
    private Pane overlayPane;
    private TextArea txtGraph;
    private TextArea txtDirections;
    private Label lblStatus;
    private Label lblScore;
    private ComboBox<String> cmbAlgorithm;

    private boolean pickingStart = false;
    private boolean pickingEnd = false;
    private static final int VIEW_SIZE = 300;

    /**
     * Constructs the main user interface for the Maze Solver application.
     * 
     * @param stage the primary application stage
     */
    public UserPane(Stage stage) {
        setPadding(new Insets(10));
        

        setupTop();
        setupLeft();
        setupCenter();
        setupRight();
        setupBottom();
        setupActions();
        // No auto-load from DB
    }

    /**
     * Configures the top navigation bar containing:
     * Maze upload controls
     * Comparison image upload controls
     * Algorithm selection options
     */
    private void setupTop() {
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Button btnUpload = new Button("Upload Maze");
        btnUpload.setOnAction(e -> loadMazeFromFile());

        Button btnUploadSecond = new Button("Upload Comparison");
        btnUploadSecond.setOnAction(e -> loadComparisonFromFile());

      

        cmbAlgorithm = new ComboBox<>();
        cmbAlgorithm.getItems().addAll("Dijkstra", "A*");
        cmbAlgorithm.setPromptText("Algorithm");

        topBar.getChildren().addAll(btnUpload, btnUploadSecond, cmbAlgorithm);
        setTop(topBar);
    }

    /**
     * Configures the left-side panel containing:
     * Maze image display
     * Comparison image display
     * Start and end point controls
     * Graph visualization overlays
     */
    private void setupLeft() {
        VBox box1 = new VBox(5);
        box1.setAlignment(Pos.CENTER);
        Label lbl1 = new Label("Maze Image (green dots = path nodes)");

        mazeContainer = new StackPane();
        mazeContainer.setPrefSize(VIEW_SIZE, VIEW_SIZE);
        mazeContainer.setMaxSize(VIEW_SIZE, VIEW_SIZE);
        mazeContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        imageView = new ImageView();
        imageView.setFitWidth(VIEW_SIZE);
        imageView.setFitHeight(VIEW_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setPickOnBounds(true);

        overlayPane = new Pane();
        overlayPane.setMouseTransparent(true);
        overlayPane.setPrefSize(VIEW_SIZE, VIEW_SIZE);

        mazeContainer.getChildren().addAll(imageView, overlayPane);
        StackPane.setAlignment(imageView, Pos.CENTER);
        StackPane.setAlignment(overlayPane, Pos.CENTER);

        box1.getChildren().addAll(lbl1, mazeContainer);

        VBox box2 = new VBox(5);
        box2.setAlignment(Pos.CENTER);
        Label lbl2 = new Label("Comparison Image");

        compareContainer = new StackPane();
        compareContainer.setPrefSize(VIEW_SIZE, VIEW_SIZE);
        compareContainer.setMaxSize(VIEW_SIZE, VIEW_SIZE);
        compareContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        imageView2 = new ImageView();
        imageView2.setFitWidth(VIEW_SIZE);
        imageView2.setFitHeight(VIEW_SIZE);
        imageView2.setPreserveRatio(true);
        imageView2.setPickOnBounds(true);

        compareContainer.getChildren().add(imageView2);
        StackPane.setAlignment(imageView2, Pos.CENTER);

        box2.getChildren().addAll(lbl2, compareContainer);

        HBox images = new HBox(10, box1, box2);
        images.setAlignment(Pos.CENTER);

        Button btnStartPoint = new Button("Select Start");
        Button btnEndPoint = new Button("Select End");
        Button btnClear = new Button("Clear Points");

        btnStartPoint.setOnAction(e -> {
            if (mazeGraph == null) { showMsg("Load a maze first"); return; }
            pickingStart = true; pickingEnd = false;
            mazeContainer.setStyle("-fx-border-color: green; -fx-border-width: 3; -fx-background-color: lightgray;");
            lblStatus.setText("CLICK ON A GREEN DOT to set START point");
        });

        btnEndPoint.setOnAction(e -> {
            if (mazeGraph == null) { showMsg("Load a maze first"); return; }
            pickingEnd = true; pickingStart = false;
            mazeContainer.setStyle("-fx-border-color: red; -fx-border-width: 3; -fx-background-color: lightgray;");
            lblStatus.setText("CLICK ON A GREEN DOT to set END point");
        });

        btnClear.setOnAction(e -> clearPoints());

        VBox controls = new VBox(10, btnStartPoint, btnEndPoint, btnClear);
        controls.setAlignment(Pos.CENTER);

        VBox leftPane = new VBox(15, images, controls);
        leftPane.setAlignment(Pos.TOP_CENTER);
        setLeft(leftPane);
    }

    /**
     * Configures the center panel used to display:
     * <ul>
     *     <li>Graph statistics</li>
     *     <li>Pathfinding results</li>
     *     <li>Similarity analysis details</li>
     * </ul>
     */
    private void setupCenter() {
        VBox center = new VBox(10);

        Label lblGraph = new Label("Graph Representation");
        txtGraph = new TextArea();
        txtGraph.setEditable(false);
        txtGraph.setPrefRowCount(6);

        Label lblDirections = new Label("Path / Directions / Similarity");
        txtDirections = new TextArea();
        txtDirections.setEditable(false);
        txtDirections.setPrefRowCount(10);

        center.getChildren().addAll(lblGraph, txtGraph, lblDirections, txtDirections);
        setCenter(center);
    }

    /**
     * Configures the right-side control panel containing:
     * Maze solving controls
     * Similarity checking controls
     * Application reset controls
     */
    private void setupRight() {
        VBox right = new VBox(15);
        right.setAlignment(Pos.TOP_CENTER);

        Button btnSolveMaze = new Button("Solve Maze");
        Button btnSimilarityCheck = new Button("Check Similarity");
        Button btnNextImage = new Button("Reset All");

        btnSolveMaze.setMaxWidth(Double.MAX_VALUE);
        btnSimilarityCheck.setMaxWidth(Double.MAX_VALUE);
        btnNextImage.setMaxWidth(Double.MAX_VALUE);

        btnSolveMaze.setOnAction(e -> solveMaze());
        btnSimilarityCheck.setOnAction(e -> checkSimilarity());
        btnNextImage.setOnAction(e -> resetAll());

        right.getChildren().addAll(btnSolveMaze, btnSimilarityCheck, btnNextImage);
        setRight(right);
    }

    /**
     * Configures the bottom status bar used to display:
     * Application status messages
     * Similarity scores
     */
    private void setupBottom() {
        HBox bottom = new HBox(20);
        bottom.setAlignment(Pos.CENTER_LEFT);

        lblStatus = new Label("Status: Ready");
        lblScore = new Label("Similarity Score: -");

        bottom.getChildren().addAll(lblStatus, lblScore);
        setBottom(bottom);
    }

    /**
     * Configures mouse interaction events for selecting
     * graph nodes directly from the displayed maze image.
     * 
     * Users may select:
     * Start nodes
     * End nodes
     */
    private void setupActions() {
        mazeContainer.setOnMouseClicked(e -> {
            if (!pickingStart && !pickingEnd) return;
            if (mazeGraph == null || mazeImage == null) return;

            double clickX = e.getX();
            double clickY = e.getY();

            double imgW = mazeImage.getWidth();
            double imgH = mazeImage.getHeight();
            double containerW = VIEW_SIZE;
            double containerH = VIEW_SIZE;

            double scale = Math.min(containerW / imgW, containerH / imgH);
            double displayedW = imgW * scale;
            double displayedH = imgH * scale;
            double offsetX = (containerW - displayedW) / 2.0;
            double offsetY = (containerH - displayedH) / 2.0;

            if (clickX < offsetX || clickX > offsetX + displayedW ||
                clickY < offsetY || clickY > offsetY + displayedH) {
                lblStatus.setText("Click inside the image area");
                return;
            }

            int origX = (int) ((clickX - offsetX) / scale);
            int origY = (int) ((clickY - offsetY) / scale);

            origX = Math.max(0, Math.min(origX, (int)imgW - 1));
            origY = Math.max(0, Math.min(origY, (int)imgH - 1));

            int graphX = origX / downsampleScale;
            int graphY = origY / downsampleScale;

            graphX = Math.max(0, Math.min(graphX, graphWidth - 1));
            graphY = Math.max(0, Math.min(graphY, graphHeight - 1));

            GraphNode node = mazeGraph.getNode(graphX, graphY);
            if (node == null) {
                lblStatus.setText("No node at graph(" + graphX + "," + graphY + ") orig(" + origX + "," + origY + ")");
                return;
            }

            if (node.isWall()) {
                lblStatus.setText("Wall at graph(" + graphX + "," + graphY + ") - click a green dot");
                return;
            }

            double screenX = offsetX + (graphX * downsampleScale + downsampleScale / 2.0) * scale;
            double screenY = offsetY + (graphY * downsampleScale + downsampleScale / 2.0) * scale;

            if (pickingStart) {
                startNode = node;
                pickingStart = false;
                mazeContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                lblStatus.setText("Start: graph(" + graphX + "," + graphY + ") orig(" + origX + "," + origY + ")");
                drawMarker(screenX, screenY, Color.GREEN);
            } else if (pickingEnd) {
                endNode = node;
                pickingEnd = false;
                mazeContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                lblStatus.setText("End: graph(" + graphX + "," + graphY + ") orig(" + origX + "," + origY + ")");
                drawMarker(screenX, screenY, Color.RED);
            }
        });
    }

    /**
     * Draws a colored marker on the overlay pane to visually
     * represent selected graph nodes.
     * 
     * @param screenX the x-coordinate on the screen
     * @param screenY the y-coordinate on the screen
     * @param color the color of the marker
     */
    private void drawMarker(double screenX, double screenY, Color color) {
        Circle marker = new Circle(6, color);
        marker.setStroke(Color.BLACK);
        marker.setStrokeWidth(1.5);
        marker.setCenterX(screenX);
        marker.setCenterY(screenY);
        overlayPane.getChildren().add(marker);
    }

    /**
     * Removes all visual markers and path overlays from the maze display.
     */
    private void clearMarkers() {
        overlayPane.getChildren().clear();
    }

    
    /**
     * Loads a maze image selected by the user and converts it
     * into a graph structure for pathfinding analysis.
     * 
     * The generated graph is visualized using node overlays
     */
    private void loadMazeFromFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fc.showOpenDialog(getScene().getWindow());
        if (file == null) return;

        try {
            mazeImage = new Image(file.toURI().toString());
            imageView.setImage(mazeImage);
            mazeGraph = ImageToGraph.convertMaze(mazeImage, file.getName());
            computeDownsampleScale();
            txtGraph.setText(mazeGraph.getStatistics());
            lblStatus.setText("Maze loaded: " + file.getName() + " | Scale=" + downsampleScale);
            clearPoints();
            visualizeGraphNodes();
        } catch (Exception ex) {
            lblStatus.setText("Load failed: " + ex.getMessage());
        }
    }

    /**
     * Loads a comparison image selected by the user and converts it
     * into a graph representation for similarity analysis.
     */
    private void loadComparisonFromFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fc.showOpenDialog(getScene().getWindow());
        if (file == null) return;

        try {
            comparisonImage = new Image(file.toURI().toString());
            imageView2.setImage(comparisonImage);
            comparisonGraph = ImageToGraph.convertToRAG(comparisonImage, file.getName(), 10);
            lblStatus.setText("Comparison loaded: " + file.getName());
        } catch (Exception ex) {
            lblStatus.setText("Load failed: " + ex.getMessage());
        }
    }

    /**
     * Calculates the downsampling scale used when converting
     * large maze images into graph structures.
     * 
     * Downsampling improves performance and reduces graph complexity.
     */
    private void computeDownsampleScale() {
        if (mazeImage == null) return;
        int w = (int) mazeImage.getWidth();
        int h = (int) mazeImage.getHeight();
        downsampleScale = 1;
        while (w / downsampleScale > 100 || h / downsampleScale > 100) {
            downsampleScale++;
        }
        graphWidth = w / downsampleScale;
        graphHeight = h / downsampleScale;
    }

    /**
     * Visualizes all graph nodes on top of the maze image
     * using graphical overlays.
     * 
     * Walkable nodes are displayed as green dots.
     */
    private void visualizeGraphNodes() {
        overlayPane.getChildren().clear();
        if (mazeGraph == null || mazeImage == null) return;

        double imgW = mazeImage.getWidth();
        double imgH = mazeImage.getHeight();
        double containerW = VIEW_SIZE;
        double containerH = VIEW_SIZE;

        double scale = Math.min(containerW / imgW, containerH / imgH);
        double displayedW = imgW * scale;
        double displayedH = imgH * scale;
        double offsetX = (containerW - displayedW) / 2.0;
        double offsetY = (containerH - displayedH) / 2.0;

        LinkedList<GraphNode> allNodes = mazeGraph.getAllNodes();
        GraphNode[] nodes = allNodes.toArray(new GraphNode[0]);

        for (int i = 0; nodes[i] != null; i++) {
            GraphNode node = nodes[i];
            if (node.isWall()) continue;

            double origX = node.getX() * downsampleScale + downsampleScale / 2.0;
            double origY = node.getY() * downsampleScale + downsampleScale / 2.0;
            double screenX = offsetX + origX * scale;
            double screenY = offsetY + origY * scale;

            Circle dot = new Circle(1.5, Color.LIME);
            dot.setCenterX(screenX);
            dot.setCenterY(screenY);
            dot.setOpacity(0.6);
            overlayPane.getChildren().add(dot);
        }
    }

    /**
     * Solves the currently loaded maze using the selected
     * graph pathfinding algorithm.
     * 
     * Supported algorithms include:
     * Dijkstra's Algorithm
     * A* Algorithm
     */
    private void solveMaze() {
        if (mazeGraph == null) { showMsg("Load a maze first"); return; }
        if (startNode == null || endNode == null) {
            showMsg("Pick start and end points first\n(start=" + (startNode!=null?"SET":"null") + ", end=" + (endNode!=null?"SET":"null") + ")");
            return;
        }

        String algo = cmbAlgorithm.getValue();
        if (algo == null) { showMsg("Select an algorithm"); return; }

        try {
            PathResult result;
            if (algo.equals("Dijkstra")) {
                result = Dijkstra.findShortestPath(mazeGraph, startNode, endNode);
            } else {
                result = AStar.findShortestPath(mazeGraph, startNode, endNode);
            }

            if (result.hasPath()) {
                txtDirections.setText(result.getPathString());
                lblStatus.setText(algo + " done. Cost: " + String.format("%.2f", result.getCost()));
                drawPathOnOverlay(result.getPath());
            } else {
                txtDirections.setText("No path found");
                lblStatus.setText("No path found");
            }
        } catch (Exception ex) {
            lblStatus.setText("Solve failed: " + ex.getMessage());
        }
    }

    /**
     * Draws the discovered shortest path on the maze overlay
     * using connected line segments.
     * 
     * @param path the list of graph nodes representing the shortest path
     */
    private void drawPathOnOverlay(java.util.List<GraphNode> path) {
        overlayPane.getChildren().removeIf(n -> n instanceof Line);

        if (mazeImage == null || path == null || path.size() < 2) return;

        double imgW = mazeImage.getWidth();
        double imgH = mazeImage.getHeight();
        double containerW = VIEW_SIZE;
        double containerH = VIEW_SIZE;

        double scale = Math.min(containerW / imgW, containerH / imgH);
        double displayedW = imgW * scale;
        double displayedH = imgH * scale;
        double offsetX = (containerW - displayedW) / 2.0;
        double offsetY = (containerH - displayedH) / 2.0;

        for (int i = 0; i < path.size() - 1; i++) {
            GraphNode a = path.get(i);
            GraphNode b = path.get(i + 1);

            double ax = offsetX + (a.getX() * downsampleScale + downsampleScale / 2.0) * scale;
            double ay = offsetY + (a.getY() * downsampleScale + downsampleScale / 2.0) * scale;
            double bx = offsetX + (b.getX() * downsampleScale + downsampleScale / 2.0) * scale;
            double by = offsetY + (b.getY() * downsampleScale + downsampleScale / 2.0) * scale;

            Line line = new Line(ax, ay, bx, by);
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(2);
            overlayPane.getChildren().add(line);
        }
    }

    /**
     * Performs graph similarity analysis between the currently
     * loaded maze graph and comparison graph.
     * 
     * The similarity score and detailed analysis results are
     * displayed to the user.
     */
    private void checkSimilarity() {
        if (mazeGraph == null || comparisonGraph == null) { showMsg("Load both images first"); return; }

        try {
            SimilarityResult result = GraphSimilarity.compare(mazeGraph, comparisonGraph);
            double score = result.getScore();
            lblScore.setText(String.format("Similarity: %.2f%%", score));
            txtDirections.setText(result.getDetails());
            lblStatus.setText("Similarity check complete");
        } catch (Exception ex) {
            lblScore.setText("Similarity: Error");
            txtDirections.setText("Similarity failed: " + ex.getMessage());
            lblStatus.setText("Similarity error: " + ex.getMessage());
        }
    }

    /**
     * Clears all selected start and end points and removes
     * visual markers from the maze display.
     */
    private void clearPoints() {
        startNode = null; endNode = null;
        pickingStart = false; pickingEnd = false;
        clearMarkers();
        if (mazeGraph != null) visualizeGraphNodes();
        mazeContainer.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
        txtDirections.clear();
        lblStatus.setText("Points cleared");
    }

    /**
     * Resets the entire application state including:
     *
     *Loaded images
     *Generated graphs
     *Selected nodes
     *Displayed results
     */
    private void resetAll() {
        mazeGraph = null; comparisonGraph = null;
        mazeImage = null; comparisonImage = null;
        startNode = null; endNode = null;
        downsampleScale = 1;
        graphWidth = 0; graphHeight = 0;
        imageView.setImage(null);
        imageView2.setImage(null);
        clearMarkers();
        txtGraph.clear(); txtDirections.clear();
        lblScore.setText("Similarity Score: -");
        lblStatus.setText("Reset");
    }

    /**
     * Displays an informational message dialog to the user.
     * 
     * @param msg the message to display
     */
    private void showMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maze Solver"); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}