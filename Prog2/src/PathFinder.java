import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class PathFinder<T> extends Application {

    private Map<T, Point2D> positions = new HashMap();
    private BorderPane root = new BorderPane();
    private Pane center = new Pane();

    ListGraph<T> graph;

    @Override
    public void start(Stage primaryStage) {
        graph = new ListGraph<>();
        primaryStage.setTitle("PathFinder");
        root = new BorderPane();
        center = new Pane();
        root.setCenter(center);
        Scene scene = new Scene(root, new Image("europa.gif").getWidth(), 785);

        Menu menu = new Menu("File");
        MenuItem newMap = new MenuItem("New Map");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem saveImage = new MenuItem("Save image");
        MenuItem exit = new MenuItem("Exit");


        HBox choiceBar = new HBox();
        menu.getItems().addAll(newMap, open, save, saveImage);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);


        Button findPath = new Button("Find Path");
        Button showConnection = new Button("Show Connection");
        Button newPlace = new Button("New Place");
        Button newConnection = new Button("New Connection");
        Button changeConncetion = new Button("Change Connection");

        choiceBar.getChildren().addAll(findPath, showConnection, newPlace, newConnection, changeConncetion);
        choiceBar.setSpacing(10);

        VBox topVBox = new VBox();

        //topVBox.setSpacing(10);

        choiceBar.setAlignment(Pos.CENTER);
        topVBox.getChildren().addAll(menuBar, choiceBar);


        newMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                javafx.scene.image.Image image = new javafx.scene.image.Image("europa.gif");
                ImageView imageView = new ImageView(image);


                center.getChildren().addAll(imageView);
                root.setCenter(center);

            }
        });

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = new File("C:\\Users\\snale\\Documents\\GitHub\\Prog2\\Prog2\\src\\europa.graph");
                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                sc.nextLine();
                String line = sc.nextLine();

                String[] lineSplit = line.split(";");

                for (int i = 0; i < lineSplit.length; i += 3) {
                    String current = lineSplit[i];
                    double xValue = parseDouble(lineSplit[i + 1]);
                    double yValue = parseDouble(lineSplit[i + 2]);
                    Point2D point = new Point2D(xValue, yValue);
                    positions.put((T) current, point);
                }

                System.out.println(positions);

                //HashMap<String, Set<Edge<T>>> tempMap = new HashMap<>();

                /*for (int i = 2; i <= 2; i++) {
                    sc.nextLine();
                    while (sc.hasNext()) {
                        sc.useDelimiter(";");

                        String current = sc.next();

                        double xValue = parseDouble(sc.next());
                        double yValue = parseDouble(sc.next());
                        System.out.println(current);
                        System.out.println(xValue);
                        System.out.println(yValue);
                        Point2D point = new Point2D(xValue, yValue);
                        positions.put((T) current, point);


                    }
                }*/


            }


        });


        root.setTop(topVBox);


        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(new Image("europa.gif").getHeight() + topVBox.getHeight());


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
