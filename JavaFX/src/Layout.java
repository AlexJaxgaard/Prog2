import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;



public class Layout extends Application {
    @Override
    public void start(Stage primaryStage) {
        FlowPane root = new FlowPane();
        root.setOrientation(Orientation.VERTICAL);
        root.getChildren().add(new Label("A"));
        root.getChildren().add(new Button("C"));
        root.getChildren().add(new Button("D"));
        root.getChildren().add(new Label("E"));
    }

    public static void main(String[] args) {
        launch(args);
    }


}