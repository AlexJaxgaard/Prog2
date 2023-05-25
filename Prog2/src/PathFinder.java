import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

// PROG2 VT2023, Inlämningsuppgift, del 2
// Grupp 112
// Alexander Jaxgård alja9460
// Emil Calmels emca3600
public class PathFinder extends Application {
    private static Map<String, Circle> positions = new HashMap<>();
    private String imagePath = "europa.gif";

    private boolean changesMade = false;

    private boolean mapOpen = false;
    private boolean stateSaved = false;

    private BorderPane root = new BorderPane();
    private Pane center = new Pane();

    private Circle[] selection = new Circle[2];
    private ListGraph<String> graph;

    @Override
    public void start(Stage primaryStage) {
        graph = new ListGraph<>();
        primaryStage.setTitle("PathFinder");
        root = new BorderPane();
        center = new Pane();
        root.setCenter(center);
        VBox topVBox = new VBox();
        Scene scene = new Scene(root, new Image(imagePath).getWidth(), new Image(imagePath).getHeight() + topVBox.getHeight() + 40);
        primaryStage.setMinWidth(scene.getWidth() + 20);
        primaryStage.setMinHeight(primaryStage.getHeight() + 200);
        Menu menu = new Menu("File");
        MenuItem newMap = new MenuItem("New Map");
        MenuItem open = new MenuItem("Open");


        MenuItem save = new MenuItem("Save");
        MenuItem saveImage = new MenuItem("Save image");
        MenuItem exit = new MenuItem("Exit");


        HBox choiceBar = new HBox();
        choiceBar.setPadding(new Insets(10));
        menu.getItems().addAll(newMap, open, save, saveImage, exit);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);


        Button findPath = new Button("Find Path");
        Button showConnection = new Button("Show Connection");
        Button newPlace = new Button("New Place");
        Button newConnection = new Button("New Connection");
        Button changeConncetion = new Button("Change Connection");

        findPath.setDisable(true);
        showConnection.setDisable(true);
        newPlace.setDisable(true);
        newConnection.setDisable(true);
        changeConncetion.setDisable(true);

        choiceBar.getChildren().addAll(findPath, showConnection, newPlace, newConnection, changeConncetion);
        choiceBar.setSpacing(10);


        //topVBox.setSpacing(10);

        choiceBar.setAlignment(Pos.CENTER);
        topVBox.getChildren().addAll(menuBar, choiceBar);

        menuBar.setId("menu");
        menu.setId("menuFile");
        newMap.setId("menuNewMap");
        open.setId("menuOpenFile");
        save.setId("menuSaveFile");
        saveImage.setId("menuSaveImage");
        exit.setId("menuExit");
        findPath.setId("btnFindPath");
        showConnection.setId("btnShowConnection");
        newPlace.setId("btnNewPlace");
        changeConncetion.setId("btnChangeConnection");
        newConnection.setId("btnNewConnection");
        center.setId("outputArea");


        class ClearMapHandler implements EventHandler<ActionEvent> {

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

                positions.clear();
                Set<String> setOfNodes = graph.getNodes();

                for (String node : setOfNodes) {
                    graph.remove(node);
                }


                center.getChildren().clear();
                for (Node node : center.getChildren()) {
                    center.getChildren().remove(node);
                    System.out.println("removed " + node);
                }

                root.getChildren().remove(center);

                root.setCenter(null);
            }
        }
        class NewMapHandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent actionEvent) {
                center.getChildren().clear();

                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);


                }
                if (mapOpen) {
                    new ClearMapHandler().handle(new ActionEvent());
                    center = new Pane();
                    center.setId("outputArea");
                }

                Image image = new Image(imagePath);

                ImageView imageView = new ImageView(image);

                imagePath = imageView.getImage().getUrl();

                findPath.setDisable(false);
                showConnection.setDisable(false);
                newPlace.setDisable(false);
                newConnection.setDisable(false);
                changeConncetion.setDisable(false);


                center.getChildren().addAll(imageView);
                root.setCenter(center);

                stateSaved = true;
                changesMade = false;
                mapOpen = true;

            }
        }
        newMap.setOnAction(new NewMapHandler());

        class MouseClickedOnCircle implements EventHandler<MouseEvent> {

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
            }
        }

        class OpenFileHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {


                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText("Unsaved changes, continue anyway?");
                    alert.setContentText("");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() != ButtonType.OK) {
                        return;
                    } else {

                        new NewMapHandler().handle(new ActionEvent());

                    }
                }
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


                File file = new File("europa.graph");


                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.show();
                    return;
                }
                if (!sc.hasNext()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.show();
                    return;
                }


                imagePath = sc.nextLine();
                java.lang.String line = sc.nextLine();

                if (line.isEmpty()) {
                    return;
                }
                if (!mapOpen) {
                    new NewMapHandler().handle(new ActionEvent());
                }
                java.lang.String[] lineSplit = line.split("\\n|;");

                for (int i = 0; i < lineSplit.length; i += 3) {
                    String current = lineSplit[i];
                    double xvalue = parseDouble(lineSplit[i + 1]);
                    double yvalue = parseDouble(lineSplit[i + 2]);
                    Circle circle = new Circle(xvalue, yvalue, 10);
                    circle.setFill(Color.BLUE);
                    circle.setOnMouseClicked(new MouseClickedOnCircle());
                    String node = (String) current;

// Check if the node already exists in positions
                    if (!positions.containsKey(node) && !center.getChildren().contains(circle)) {
                        positions.put(node, circle);
                        graph.add(node);
                        center.getChildren().add(circle);
                    }
                }


                for (int i = 0; i < center.getChildren().size(); i++) {
                    if (center.getChildren().get(i) instanceof Circle) {
                        center.getChildren().get(i).setViewOrder(-1);
                    }
                }

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
                    java.lang.String node = newScanner.next();


                    java.lang.String destination = newScanner.next();


                    java.lang.String name = newScanner.next();


                    java.lang.String weight = newScanner.next();
                    int newWeight = Integer.parseInt(weight);


                    if (!graph.pathExists((String) node, (String) destination)) {
                        graph.connect((String) node, (String) destination, name, newWeight);

                    }
                    drawLines(getPosition(node), getPosition(destination));


                }

                changesMade = false;
                stateSaved = true;
            }


        }
        open.setOnAction(new OpenFileHandler());

        class NewConnectionHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                String node1 = "";
                String node2 = "";
                String nameOfConnection = "";
                int timeOfConnection = 0;


                Edge<String> edgeBetween = null;
                for (String point : positions.keySet()) {
                    if (!graph.getNodes().contains(point)) {
                        graph.add(point);
                    }


                }
                if (selection[0] == null || selection[1] == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("");
                    alert.setTitle("Error!");
                    alert.setContentText("Two places must be selected!");

                    alert.show();
                    return;
                }

                node1 = getName(selection[0]);
                node2 = getName(selection[1]);

                if (node1 == null) {
                    throw new IllegalStateException("Node1 is null");
                }
                if (node2 == null) {
                    throw new IllegalStateException("Node2 is null");
                }

                try {
                    edgeBetween = graph.getEdgeBetween(node1, node2);
                } catch (NoSuchElementException noSuchElementException) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Node doesn't exist");
                    alert.show();
                    return;
                }

                if (edgeBetween != null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Path already exists!");
                    alert.show();
                    return;
                }


                TextInputDialog td = new TextInputDialog();
                GridPane gridPane = new GridPane();
                td.setTitle("Connection");
                td.setHeaderText("Connection from " + node1 + " to " + node2);
                TextField nameField = new TextField();
                TextField weightField = new TextField();
                gridPane.addRow(0, new Label("Name:"), nameField);
                gridPane.addRow(1, new Label("Time:"), weightField);
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                gridPane.setAlignment(Pos.CENTER);
                td.getDialogPane().setContent(gridPane);


                Optional<java.lang.String> result = td.showAndWait();

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


                drawLines(getPosition((java.lang.String) node1), getPosition((java.lang.String) node2));
                stateSaved = false;
                changesMade = true;

            }
        }
        newConnection.setOnAction(new NewConnectionHandler());

        class SaveFileHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                String path = "europa.graph";
                FileWriter myWriter = null;
                try {
                    myWriter = new FileWriter(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                java.lang.String file = "file:" + imagePath;
                java.lang.String cityAndPositions = "";
                java.lang.String nodesAndDetails = "";
                int counter = 0;

                for (Map.Entry<String, Circle> entry : positions.entrySet()) {

                    if (counter >= 1) {
                        cityAndPositions += ";";

                    }
                    cityAndPositions += entry.getKey() + ";";
                    cityAndPositions += entry.getValue().getCenterX() + ";" + entry.getValue().getCenterY();
                    counter++;


                }

                for (String node : graph.getNodes()) {

                    for (Edge<String> edge : graph.getEdgesFrom(node)) {
                        nodesAndDetails += node + ";" + edge.getDestination() + ";" + edge.getName() + ";" + edge.getWeight() + "\n";

                    }

                }

                java.lang.String toWrite = file + "\n" + cityAndPositions + "\n" + nodesAndDetails;

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
        save.setOnAction(new SaveFileHandler());


        class SaveImageHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (selection[0] != null && selection[1] == null) {
                    selection[0].setFill(Color.BLUE);
                } else if (selection[0] == null && selection[1] != null) {
                    selection[1].setFill(Color.BLUE);
                } else if (selection[0] != null && selection[1] != null) {
                    selection[0].setFill(Color.BLUE);
                    selection[1].setFill(Color.BLUE);
                }

                WritableImage image = center.snapshot(new SnapshotParameters(), null);


                File file = new File("./src/capture.png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException e) {

                }
                if (selection[0] != null && selection[1] == null) {
                    selection[0].setFill(Color.RED);
                } else if (selection[0] == null && selection[1] != null) {
                    selection[1].setFill(Color.RED);
                } else if (selection[0] != null && selection[1] != null) {
                    selection[0].setFill(Color.RED);
                    selection[1].setFill(Color.RED);
                }
            }

        }
        saveImage.setOnAction(new SaveImageHandler());


        class ExitProgramHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {
                if (changesMade && !stateSaved) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning!");
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
                    alert.setTitle("Warning!");
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
        exit.setOnAction(new ExitProgramHandler());

        class MouseClickedOnMap implements EventHandler<MouseEvent> {

            @Override
            public void handle(MouseEvent mouseEvent) {
                double xvalue = mouseEvent.getX();
                double yvalue = mouseEvent.getY();

                TextInputDialog td = new TextInputDialog("");

                td.setHeaderText("Name of place:");
                td.setTitle("Name");
                Optional<java.lang.String> result = td.showAndWait();

                center.setCursor(Cursor.DEFAULT);
                newPlace.setDisable(false);
                center.setOnMouseClicked(null);
                java.lang.String nameOfPlace = td.getResult();

                if (nameOfPlace == null || nameOfPlace.isEmpty()) {
                    return;
                }

                /*if (positions.containsKey(nameOfPlace)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("A place with the same name already exists!");
                    alert.show();
                    return;
                }*/


                Circle circle = new Circle(xvalue, yvalue, 10, Color.BLUE);

                circle.setOnMouseClicked(new MouseClickedOnCircle());
                circle.setId(nameOfPlace);
                String node = (String) nameOfPlace;
                positions.put(node, circle);
                graph.add(node);
                center.getChildren().add(circle);
                for (String point : positions.keySet()) {
                    if (!graph.getNodes().contains(point)) {
                        graph.add(point);
                    }


                }
                for (int i = 0; i < center.getChildren().size(); i++) {
                    if (center.getChildren().get(i) instanceof Circle) {
                        center.getChildren().get(i).setViewOrder(-1);
                    }
                }

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

                Edge<String> edge = graph.getEdgeBetween(getName(selection[0]), getName(selection[1]));

                java.lang.String name = edge.getName();
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

        class NewPlaceHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {

                newPlace.setDisable(true);
                center.setCursor(Cursor.CROSSHAIR);
                center.setOnMouseClicked(new MouseClickedOnMap());


            }
        }

        newPlace.setOnAction(new NewPlaceHandler());
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

                Edge<String> edge = graph.getEdgeBetween(getName(selection[0]), getName(selection[1]));

                java.lang.String name = edge.getName();
                int weight = edge.getWeight();

                TextInputDialog td = new TextInputDialog();
                GridPane gridPane = new GridPane();
                td.setTitle("Connection");
                td.setHeaderText("Connection from " + getName(selection[0]) + " to " + getName(selection[1]));
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
                Optional<java.lang.String> result = td.showAndWait();

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

        class FindPathHandler implements EventHandler<ActionEvent> {

            @Override
            public void handle(ActionEvent actionEvent) {


                int totalTime = 0;
                String node1 = getName(selection[0]);
                String node2 = getName(selection[1]);
                List<Edge<String>> setOfEdges;
                try {
                    setOfEdges = graph.getPath(node1, node2);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("Two places must be selected!");

                    alert.show();
                    return;
                }

                if (setOfEdges == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setContentText("No route between " + getName(selection[0]) + " and " + getName(selection[1]));

                    alert.show();
                    return;
                }


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("The Path from " + node1 + " to " + node2 + ":");
                TextArea textArea = new TextArea();
                textArea.setEditable(false);


                for (Edge<String> edge : setOfEdges) {
                    String destination = edge.getDestination();
                    java.lang.String name = edge.getName();
                    int time = edge.getWeight();
                    totalTime += time;
                    textArea.appendText("to " + destination + " by " + name + " takes " + time + "\n");
                }
                textArea.appendText("Total: " + totalTime);

                alert.getDialogPane().setContent(textArea);

                alert.show();

            }
        }
        findPath.setOnAction(new FindPathHandler());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(new Image(imagePath).getHeight() + topVBox.getHeight() + 40);


    }


    private Point2D getPosition(java.lang.String name) {
        for (Map.Entry<String, Circle> entry : positions.entrySet()) {
            if (entry.getKey().equals(name)) {
                return new Point2D(entry.getValue().getCenterX(), entry.getValue().getCenterY());
            }
        }
        return null;


    }

    private String getImagePath() {
        File file = new File("europa.graph");
        Scanner newScanner = null;
        try {
            newScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return newScanner.nextLine();


    }

    private void drawLines(Point2D from, Point2D to) {
        Line line = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        center.getChildren().add(line);

    }

    public static String getName(Circle circle) {


        for (Map.Entry<String, Circle> entry : positions.entrySet()) {

            if (entry.getValue().equals(circle)) {
                return entry.getKey();
            }


        }


        return null;


    }


    public static void main(java.lang.String[] args) {
        launch(args);

    }


}