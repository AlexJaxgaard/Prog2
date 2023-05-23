import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class PathFinder<T> extends Application {
    private boolean mapOpen = false;
    private boolean mapChanged = false;

    private boolean changesSaved = false;
    private Map<T, Point2D> positions = new HashMap();

    private Map<T, Circle> circles = new HashMap();
    private BorderPane root = new BorderPane();
    private Pane center = new Pane();

    ListGraph<NodeOnMap> graph;

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
        menu.getItems().addAll(newMap, open, save, saveImage, exit);
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
                    NodeOnMap node = new NodeOnMap(current, xValue, yValue);
                    center.getChildren().add(node);
                    graph.add(node);


                }


                System.out.println(graph.getNodes());

                //Add connections

                sc.close();
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
                    String lineToCheck = newScanner.nextLine();
                    String[] values = lineToCheck.split(";");

                    if (values.length < 4) {
                        // Handle invalid line here
                        continue;
                    }

                    String node = values[0].trim();
                    System.out.println("node: " + node);

                    String destination = values[1].trim();
                    System.out.println("destination: " + destination);

                    String name = values[2].trim();
                    System.out.println("name: " + name);

                    int weight = Integer.parseInt(values[3].trim());
                    System.out.println("weight: " + weight);
                    NodeOnMap start = new NodeOnMap(node, positions.get((T) node).getX(), positions.get((T) node).getY());
                    NodeOnMap finish = new NodeOnMap(destination, positions.get((T) destination).getX(), positions.get((T) destination).getY());
                    if (!graph.pathExists(start, finish)) {
                        graph.connect(start, finish, name, weight);

                    }
                    drawLines(getPosition(node), getPosition(destination));
                    System.out.println("Drew lines");

                }


            }


        }
        open.setOnAction(new OpenFileHandler());

        class saveFileHandler implements EventHandler<ActionEvent> {


            @Override
            public void handle(ActionEvent actionEvent) {
                mapChanged = false;
                changesSaved = true;
                String file = "file:europa.graph";
                String cityAndPositions = "";
                String nodesAndDetails = "";
                int counter = 0;
                for (Map.Entry<T, Point2D> entry : positions.entrySet()) {

                    if (counter >= 1) {
                        cityAndPositions += ";";

                    }
                    cityAndPositions += entry.getKey() + ";";
                    cityAndPositions += entry.getValue().getX() + ";" + entry.getValue().getY();
                    counter++;

                    System.out.println(cityAndPositions);
                }

                for (NodeOnMap node : graph.getNodes()) {

                    for (Edge<NodeOnMap> edge : graph.getEdgesFrom(node)) {
                        nodesAndDetails += node + ";" + edge.getDestination() + ";" + edge.getName() + ";" + edge.getWeight() + "\n";

                    }

                }

                String toWrite = file + "\n" + cityAndPositions + "\n" + nodesAndDetails;

                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter("C:\\Users\\snale\\Documents\\GitHub\\Prog2\\Prog2\\src\\europa.graph");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    myWriter.write(toWrite);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    myWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        save.setOnAction(new saveFileHandler());

        class mapClicked implements EventHandler<MouseEvent> {

            @Override
            public void handle(MouseEvent mouseEvent) {
                for (T node : positions.keySet()) {
                    if (positions.get(node).getX() == mouseEvent.getX() && positions.get(node).getY() == mouseEvent.getY()) {
                        System.out.println("Match found!" + positions.get(node));
                    }
                }
            }
        }


        class saveImageHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {

                WritableImage image = center.snapshot(new SnapshotParameters(), null);


                File file = new File("./src/capture.png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException e) {
                    System.out.println("Fel när filen skulle skapas");
                }
            }

        }
        saveImage.setOnAction(new saveImageHandler());


        class exitProgramHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Warning!");
                alert.setContentText("Unsaved changes, continue anyways?");


                if (mapChanged == true && changesSaved == false) {
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        Platform.exit();
                    } else if (alert.getResult() == ButtonType.CANCEL) {
                        return;
                    }
                } else {
                    Platform.exit();
                }


            }


        }
        exit.setOnAction(new exitProgramHandler());

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Warning!");
                alert.setContentText("Unsaved changes, continue anyways?");


                if (mapChanged && !changesSaved) {
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        Platform.exit();
                    } else if (alert.getResult() == ButtonType.CANCEL) {
                        windowEvent.consume();
                    }
                }


            }


        });


        class mouseClickedOnMap implements EventHandler<MouseEvent> {

            @Override
            public void handle(MouseEvent mouseEvent) {
                double xValue = mouseEvent.getX();
                double yValue = mouseEvent.getY();
                TextInputDialog td = new TextInputDialog("");

                td.setHeaderText("Enter name of place:");
                td.setTitle("New place");


                td.showAndWait();
                if (td.getResult() == (null) || td.getResult().isEmpty()) {
                    return;
                } else {
                    String nameOfPlace = td.getResult();


                    System.out.println(nameOfPlace);

                    graph.add(new NodeOnMap(nameOfPlace, xValue, yValue));

                    positions.put((T) nameOfPlace, new Point2D(xValue, yValue));
                    drawCircles((T) nameOfPlace);

                    center.setCursor(Cursor.DEFAULT);
                    newPlace.setDisable(false);
                    mapChanged = true;
                    changesSaved = false;
                }

                center.setOnMouseClicked(null);
            }
        }


        class newPlaceHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {

                newPlace.setDisable(true);
                center.setCursor(Cursor.CROSSHAIR);
                center.setOnMouseClicked(new mouseClickedOnMap());


                //center.setOnMouseClicked();


            }
        }
        newPlace.setOnAction(new newPlaceHandler());

        class newConnectionHandler implements EventHandler<ActionEvent> {


            @Override
            public void handle(ActionEvent actionEvent) {

            }
        }

        newConnection.setOnAction(new newConnectionHandler());


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


    class circlePressedHandler implements EventHandler<MouseEvent> {

        int nodesPressed = 0;

        @Override
        public void handle(MouseEvent mouseEvent) {
            for (Node node : center.getChildren()) {
                if (node.isPressed()) {
                    System.out.println("node pressed");
                    nodesPressed++;
                    if (nodesPressed >= 2) {

                    }
                }
            }

        }
    }

    private void drawLines(Point2D from, Point2D to) {
        Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        center.getChildren().add(line);

    }

    private void drawCircles(T point) {
        Circle circle = new Circle(positions.get(point).getX(), positions.get(point).getY(), 10);

        circle.setOnMouseClicked(new circlePressedHandler());
        Button button = new Button();
        center.getChildren().add(circle);

        NodeOnMap nodeOnMap = new NodeOnMap(point.toString(), positions.get(point).getX(), positions.get(point).getY());

        graph.add(nodeOnMap);
        circles.put(point, circle);

    }


    public static void main(String[] args) {
        launch(args);

    }


}
