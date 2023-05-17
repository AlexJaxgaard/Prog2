import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class Testing extends Application{



        @Override
        public void start(Stage primaryStage) {
            FlowPane root = new FlowPane();
            root.setOrientation(Orientation.HORIZONTAL);
            root.getChildren().add(new Label("A"));
            root.getChildren().add(new Button("C"));
            root.getChildren().add(new Button("D"));
            root.getChildren().add(new Label("E"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }






}