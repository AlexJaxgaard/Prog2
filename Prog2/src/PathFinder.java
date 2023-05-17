import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
        MenuItem menuItem1 = new MenuItem("New Map");
        MenuItem menuItem2 = new MenuItem("Open");
        MenuItem menuItem3 = new MenuItem("Save");
        MenuItem menuItem4 = new MenuItem("Save image");
        MenuItem menuItem5 = new MenuItem("Exit");

        menuItem1.setOnAction(actionEvent -> {

        });


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

    }

    public class openFileHandler implements EventHandler{

    }


    public static void main(String[] args) {
        launch(args);
    }




}
