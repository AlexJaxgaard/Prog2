import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class NodeOnMap extends Circle {
    private Circle circle;
    String name;
    Double x;
    Double y;

    private boolean selected = false;

    public NodeOnMap(String name, Double x, Double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        circle = new Circle(x, y, 10);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public Circle select() {
        this.setFill(Color.RED);
        selected = true;
        return this;
    }

    public Circle unselect() {
        this.setFill(Color.BLUE);
        selected = false;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public Circle createCircle() {
        this.setCenterX(x);
        this.setCenterY(y);
        this.setRadius(10);
        this.setFill(Color.BLUE);
        return this;
    }

    public Circle getCircle() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}


