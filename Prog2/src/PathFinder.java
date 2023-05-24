import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

public class PathFinder<T> extends Application {

    private boolean changesMade = false;

    private boolean mapOpen = false;
    private boolean stateSaved = false;
    private Map<T, Circle> positions = new HashMap();
    private BorderPane root = new BorderPane();
    private Pane center = new Pane();

    private Circle[] selection = new Circle[2];
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

        class ClearMapHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                positions.clear();
                Set<T> setOfNodes = graph.getNodes();

                for (T node : setOfNodes) {
                    graph.remove(node);
                }


                center.getChildren().clear();
                root.setCenter(null);
            }
        }
        class newMapHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {
                center.getChildren().clear();

                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);


                }
                if (mapOpen) {
                    new ClearMapHandler().handle(new ActionEvent());
                }
                javafx.scene.image.Image image = new javafx.scene.image.Image("europa.gif");
                ImageView imageView = new ImageView(image);


                center.getChildren().addAll(imageView);
                root.setCenter(center);
                stateSaved = true;
                changesMade = false;
                mapOpen = true;

            }
        }
        newMap.setOnAction(new newMapHandler());

        class mouseClickedOnCircle implements EventHandler<MouseEvent> {

            @Override
            public void handle(MouseEvent mouseEvent) {


                Circle circle = (Circle) mouseEvent.getSource();

                if (circle.getFill() == Color.BLUE) {
                    if (selection[0] == null && selection[1] != null) {
                        selection[0] = circle;
                        selection[0].setFill(Color.RED);
                    } else if (selection[0] != null && selection[1] == null) {
                        selection[1] = circle;
                        selection[1].setFill(Color.RED);
                    } else if (selection[0] == null && selection[1] == null) {
                        selection[0] = circle;
                        selection[0].setFill(Color.RED);
                    }
                } else if (circle.getFill() == Color.RED) {
                    if (selection[0] == circle) {
                        selection[0].setFill(Color.BLUE);
                        selection[0] = null;
                    } else if (selection[1] == circle) {
                        selection[1].setFill(Color.BLUE);
                        selection[1] = null;
                    }
                }


                for (T node : positions.keySet()) {

                }
            }
        }

        class OpenFileHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (selection[0] != null && selection[1] == null) {
                    selection[0].setFill(Color.BLUE);
                    selection[0] = null;
                } else if (selection[0] == null && selection[1] != null) {
                    selection[1].setFill(Color.BLUE);
                    selection[1] = null;
                } else if (selection[0] != null && selection[1] != null) {
                    selection[0].setFill(Color.BLUE);
                    selection[1].setFill(Color.BLUE);
                    selection[0] = null;
                    selection[1] = null;
                }


                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("You have unsaved changes, if you choose to continue, any unsaved progress will be overwritten.");
                    alert.setContentText("Are you sure you want to continue?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() != ButtonType.OK) {
                        return;
                    } else {
                        new ClearMapHandler().handle(new ActionEvent());
                        new newMapHandler().handle(new ActionEvent());

                    }
                }

                new newMapHandler().handle(new ActionEvent());


                File file = new File("C:\\Users\\snale\\Documents\\GitHub\\Prog2\\Prog2\\src\\europa.graph");
                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.show();
                }

                sc.nextLine();
                String line = sc.nextLine();

                String[] lineSplit = line.split(";");

                for (int i = 0; i < lineSplit.length; i += 3) {
                    String current = lineSplit[i];
                    double xValue = parseDouble(lineSplit[i + 1]);
                    double yValue = parseDouble(lineSplit[i + 2]);
                    Circle circle = new Circle(xValue, yValue, 10);
                    circle.setFill(Color.BLUE);
                    circle.setOnMouseClicked(new mouseClickedOnCircle());
                    System.out.println("Added click functionality for " + current);
                    positions.put((T) current, circle);
                }


                System.out.println(positions);
                //Adds points
                for (T point : positions.keySet()) {
                    center.getChildren().add((positions.get(point)));
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

                newScanner.nextLine();
                newScanner.nextLine();

                newScanner.useDelimiter(Pattern.compile("(\\n)|;"));
                while (newScanner.hasNextLine()) {


                    if (!newScanner.hasNext()) {
                        return;
                    }
                    String node = newScanner.next();


                    String destination = newScanner.next();


                    String name = newScanner.next();


                    String weight = (newScanner.next());
                    int newWeight = Integer.parseInt(weight);


                    if (!graph.pathExists((T) node, (T) destination)) {
                        graph.connect((T) node, (T) destination, name, newWeight);

                    }
                    drawLines(getPosition(node), getPosition(destination));
                    System.out.println("Drew lines");
                    System.out.println(graph.getNodes());
                }

                changesMade = false;
                stateSaved = true;
            }


        }
        open.setOnAction(new OpenFileHandler());

        class NewConnectionHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                T node1 = (T) "";
                T node2 = (T) "";
                String nameOfConnection = "";
                int timeOfConnection = 0;


                if (selection[0] == null || selection[1] == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("Two places must be selected!");

                    alert.show();
                    return;
                }

                for (Map.Entry<T, Circle> entry : positions.entrySet()) {
                    if (entry.getValue() == selection[0]) {
                        node1 = entry.getKey();
                    }
                    if (entry.getValue() == selection[1]) {
                        node2 = entry.getKey();
                    }
                }

                if (graph.pathExists(node1, node2)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Path already exists!");
                    alert.show();
                    return;
                }
                TextInputDialog td = new TextInputDialog();
                GridPane gridPane = new GridPane();
                td.setTitle("Connection");
                td.setHeaderText("Create connection between " + node1 + " to " + node2);
                TextField nameField = new TextField();
                TextField weightField = new TextField();
                gridPane.addRow(0, new Label("Name:"), nameField);
                gridPane.addRow(1, new Label("Time:"), weightField);
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                gridPane.setAlignment(Pos.CENTER);
                td.getDialogPane().setContent(gridPane);


                Optional<String> result = td.showAndWait();

                if (!result.isPresent()) {
                    return;
                }

                if (nameField.getCharacters().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Name cannot be empty!");
                    alert.show();
                    return;
                }

                nameOfConnection = nameField.getCharacters().toString();


                if (weightField.getCharacters().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Time cannot be empty!");
                    alert.show();
                    return;
                }

                try {
                    timeOfConnection = Integer.parseInt(weightField.getCharacters().toString());
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Time must be a number!");
                    alert.show();
                    return;
                }

                graph.connect(node1, node2, nameField.getCharacters().toString(), timeOfConnection);
                System.out.println("Connected nodes");

                drawLines(getPosition((String) node1), getPosition((String) node2));
                stateSaved = false;
                changesMade = true;

            }
        }
        newConnection.setOnAction(new NewConnectionHandler());

        class saveFileHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                String file = "file:europa.graph";
                String cityAndPositions = "";
                String nodesAndDetails = "";
                int counter = 0;

                for (Map.Entry<T, Circle> entry : positions.entrySet()) {

                    if (counter >= 1) {
                        cityAndPositions += ";";

                    }
                    cityAndPositions += entry.getKey() + ";";
                    cityAndPositions += entry.getValue().getCenterX() + ";" + entry.getValue().getCenterY();
                    counter++;

                    System.out.println(cityAndPositions);
                }

                for (T node : graph.getNodes()) {

                    for (Edge<T> edge : graph.getEdgesFrom(node)) {
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

                changesMade = false;
                stateSaved = true;

            }
        }
        save.setOnAction(new saveFileHandler());


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
                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Unsaved changes, exit anyway?");
                    alert.setContentText("");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() != ButtonType.OK) {
                        actionEvent.consume();
                    } else {
                        Platform.exit();
                    }

                } else if (!changesMade && stateSaved) {
                    Platform.exit();
                }


            }
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Unsaved changes, exit anyway?");
                    alert.setContentText("");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() != ButtonType.OK) {
                        windowEvent.consume();
                    } else {
                        Platform.exit();
                    }


                }

            }
        });
        exit.setOnAction(new exitProgramHandler());

        class mouseClickedOnMap implements EventHandler<MouseEvent> {

            @Override
            public void handle(MouseEvent mouseEvent) {
                double xValue = mouseEvent.getX();
                double yValue = mouseEvent.getY();
                TextInputDialog td = new TextInputDialog("");

                td.setHeaderText("Enter name of place:");
                td.setTitle("New place");
                Optional<String> result = td.showAndWait();

                center.setCursor(Cursor.DEFAULT);
                newPlace.setDisable(false);
                center.setOnMouseClicked(null);
                String nameOfPlace = td.getResult();
                if (!result.isPresent()) {
                    return;

                }


                System.out.println(nameOfPlace);
                graph.add((T) nameOfPlace);


                Circle circle = new Circle(xValue, yValue, 10, Color.BLUE);
                circle.setOnMouseClicked(new mouseClickedOnCircle());
                positions.put((T) nameOfPlace, circle);
                center.getChildren().add(circle);


                changesMade = true;
                stateSaved = false;

            }
        }

        class ShowConnectionHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (selection[0] == null || selection[1] == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("Two places must be selected!");

                    alert.show();
                    return;
                }
                if (graph.getEdgeBetween(getName(selection[0]), getName(selection[1])) == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("No edge between " + getName(selection[0]) + " and " + getName(selection[1]));

                    alert.show();
                    return;
                }

                Edge<T> edge = graph.getEdgeBetween(getName(selection[0]), getName(selection[1]));

                String name = edge.getName();
                int weight = edge.getWeight();
                TextInputDialog td = new TextInputDialog();
                GridPane gridPane = new GridPane();
                td.setTitle("Connection");
                td.setHeaderText("Connection from " + getName(selection[0]) + " to " + getName(selection[1]));
                TextField nameField = new TextField();
                nameField.replaceText(0, 0, name);
                nameField.setEditable(false);
                TextField weightField = new TextField();
                weightField.replaceText(0, 0, weight + "");
                weightField.setEditable(false);
                gridPane.addRow(0, new Label("Name:"), nameField);
                gridPane.addRow(1, new Label("Time:"), weightField);
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                gridPane.setAlignment(Pos.CENTER);
                td.getDialogPane().setContent(gridPane);
                td.showAndWait();
            }
        }
        showConnection.setOnAction(new ShowConnectionHandler());

        class newPlaceHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {

                newPlace.setDisable(true);
                center.setCursor(Cursor.CROSSHAIR);
                center.setOnMouseClicked(new mouseClickedOnMap());


            }
        }
        newPlace.setOnAction(new newPlaceHandler());
        root.setTop(topVBox);

        class ChangeConnectionHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (selection[0] == null || selection[1] == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("Two places must be selected!");

                    alert.show();
                    return;
                }
                if (graph.getEdgeBetween(getName(selection[0]), getName(selection[1])) == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("No edge between " + getName(selection[0]) + " and " + getName(selection[1]));

                    alert.show();
                    return;
                }

                Edge<T> edge = graph.getEdgeBetween(getName(selection[0]), getName(selection[1]));

                String name = edge.getName();
                int weight = edge.getWeight();

                TextInputDialog td = new TextInputDialog();
                GridPane gridPane = new GridPane();
                td.setTitle("Connection");
                td.setHeaderText("Change connection from " + getName(selection[0]) + " to " + getName(selection[1]));
                TextField nameField = new TextField();
                nameField.replaceText(0, 0, name);
                nameField.setEditable(false);

                TextField weightField = new TextField();


                weightField.setEditable(true);
                weightField.replaceText(0, 0, weight + "");
                gridPane.addRow(0, new Label("Name:"), nameField);
                gridPane.addRow(1, new Label("Time:"), weightField);
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                gridPane.setAlignment(Pos.CENTER);
                td.getDialogPane().setContent(gridPane);
                Optional<String> result = td.showAndWait();

                try {
                    graph.setConnectionWeight(getName(selection[0]), getName(selection[1]), Integer.parseInt(weightField.getCharacters().toString()));
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Weight must be a numeric type!");
                    alert.show();
                }


            }
        }
        changeConncetion.setOnAction(new ChangeConnectionHandler());

        class 

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(new Image("europa.gif").getHeight() + topVBox.getHeight());


    }


    private Point2D getPosition(String name) {
        for (Map.Entry<T, Circle> entry : positions.entrySet()) {
            if (entry.getKey().equals(name)) {
                return new Point2D(entry.getValue().getCenterX(), entry.getValue().getCenterY());
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

    public T getName(Circle circle) {

        for (Map.Entry<T, Circle> entry : positions.entrySet()) {
            if (entry.getValue() == circle) {
                return entry.getKey();
            }
        }
        return null;


    }


    public static void main(String[] args) {
        launch(args);

    }


}
