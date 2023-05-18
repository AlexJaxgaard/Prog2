import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class PathFinder<T> extends Application {
    private boolean mapOpen = false;
    private boolean mapChanged = false;
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


        class newMapHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (mapChanged) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);


                }

                javafx.scene.image.Image image = new javafx.scene.image.Image("europa.gif");
                ImageView imageView = new ImageView(image);


                center.getChildren().addAll(imageView);
                root.setCenter(center);
                mapOpen = true;


            }
        }
        newMap.setOnAction(new newMapHandler());
        class OpenFileHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {

                if (!mapOpen) {

                    new newMapHandler().handle(new ActionEvent());


                }
                if (mapChanged || mapOpen) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Map already exists");
                    alert.setHeaderText("You already have a map open, if you choose to continue, any unsaved progress will be overwritten.");
                    alert.setContentText("Are you sure you want to continue?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() != ButtonType.OK) {
                        return;
                    }
                }

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
                //Adds points
                for (T point : positions.keySet()) {
                    Circle circle = new Circle(positions.get(point).getX(), positions.get(point).getY(), 10);
                    center.getChildren().add(circle);
                    graph.add(point);

                }
                System.out.println(graph.getNodes());

                //Add connections


                Scanner newScanner = null;
                try {
                    newScanner = new Scanner(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                newScanner.useDelimiter(";|\\r\\n");
                newScanner.nextLine();
                newScanner.nextLine();


                while (newScanner.hasNextLine()) {


                    if (!newScanner.hasNext()) {
                        return;
                    }
                    String node = newScanner.next();

                    System.out.println("node: " + node);
                    String destination = newScanner.next();
                    System.out.println("destination: " + destination);
                    String name = newScanner.next();
                    System.out.println("name: " + name);
                    int weight = Integer.parseInt((newScanner.next()));
                    System.out.println("weight: " + weight);

                    if (!graph.pathExists((T) node, (T) destination)) {
                        graph.connect((T) node, (T) destination, name, weight);

                    }
                    drawLines(getPosition(node), getPosition(destination));
                    System.out.println("Drew lines");

                }


            }


        }
        open.setOnAction(new OpenFileHandler());

        root.setTop(topVBox);


        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(new Image("europa.gif").getHeight() + topVBox.getHeight());


    }


    private Point2D getPosition(String name) {
        for (Map.Entry<T, Point2D> entry : positions.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }
        return null;


    }

    private void drawLines(Point2D from, Point2D to) {
        Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        center.getChildren().add(line);

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
