import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PathFinder<T> extends Application {
    private BorderPane root = new BorderPane();
    private Pane center = new Pane();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("PathFinder");
        root = new BorderPane();
        center = new Pane();
        root.setCenter(center);

        Menu menu = new Menu("File");
        MenuItem newMap = new MenuItem("New Map");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveImage = new MenuItem("Save image");
        MenuItem exit = new MenuItem("Exit");

        newMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                javafx.scene.image.Image image = new javafx.scene.image.Image(
                        "file:C:\\Users\\snale\\Documents\\GitHub\\Prog2\\Prog2\\src\\europa.gif");
                ImageView imageView = new ImageView(image);
                root.setCenter(imageView);
            }
        });

        menu.getItems().addAll(newMap, open, save, saveImage);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);



        Scene scene = new Scene(root);

        root.setTop(menuBar);



        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static class openFileHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {

        }
    }


    public static void main(String[] args) {
        launch(args);
    }




}
