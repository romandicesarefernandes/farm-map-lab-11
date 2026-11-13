import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/** Leaf: a single item (cow, milk storage, tractor, tiller, drone, soy crop). */
public class Item implements FarmItem {
    private final String name;
    private final double price, x, y, width, height;

    public Item(String name, double price, double x, double y, double width, double height) {
        this.name = name;
        this.price = price;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override public String getName() { return name; }
    @Override public double getPrice() { return price; }
    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }

    @Override
    public void draw(Pane pane) {
        Rectangle box = new Rectangle(x, y, width, height);
        box.setFill(Color.LIGHTGREEN);
        box.setStroke(Color.DARKGREEN);
        Text label = new Text(x + 4, y + 14, name);
        pane.getChildren().addAll(box, label);
    }

    @Override
    public void collect(List<FarmItem> out) {
        out.add(this);
    }

    @Override
    public String toString() { return name; }
}
