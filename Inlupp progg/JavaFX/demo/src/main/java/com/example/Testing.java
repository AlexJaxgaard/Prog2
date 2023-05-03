import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Testing extends Application{

	@Override
	public void start(Stage primaryStage) {
		Pane root = new FlowPane();
		
		Label label = new Label("Det funkar!");
		root.getChildren().add(label);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
