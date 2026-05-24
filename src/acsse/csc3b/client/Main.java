package acsse.csc3b.client;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Maze Solver application.
 * 
 * This class launches the JavaFX application and initializes
 * the primary user interface window.
 * 
 * 
 * The application provides functionality for:
 * 
 *Loading maze images
 *Generating graph structures from images
 *Solving mazes using graph algorithms
 *Performing graph similarity analysis
 * 
 * @author The Maze Solvers
 * @version 1.0
 */
public class Main extends Application {

	/**
     * Launches the JavaFX application.
     * 
     * @param args command-line arguments
     */
	public static void main(String[] args)
	{
		launch(args);
	}
	
	/**
     * Initializes and displays the main application window.
     * 
     * @param primaryStage the main stage for the JavaFX application
     * @throws Exception if an error occurs during initialization
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		UserPane root = new UserPane(primaryStage);
		Scene scene = new Scene(root,1000,700);
		primaryStage.setTitle("Maze Solver");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
