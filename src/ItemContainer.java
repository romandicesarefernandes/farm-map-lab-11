import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/** Composite: a container (Farm, Barn, Livestock Area, ...) that holds FarmItems. */
public class ItemContainer implements FarmItem {
    private final String name;
    private final double price, x, y, width, height;
    private final List<FarmItem> children = new ArrayList<>();

    public ItemContainer(String name, double price, double x, double y, double width, double height) {
        this.name = name;
        this.price = price;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void add(FarmItem child) {
        children.add(child);
    }

    @Override public String getName() { return name; }

    @Override
    public double getPrice() {
        double total = price;
        for (FarmItem child : children) {
            total += child.getPrice();
        }
        return total;
    }

    @Override public double getX() { return x; }
    @Override public double getY() { return y; }
    @Override public double getWidth() { return width; }
    @Override public double getHeight() { return height; }

    @Override
    public void draw(Pane pane) {
        // Draw self first so the outline sits underneath the children.
        Rectangle box = new Rectangle(x, y, width, height);
        box.setFill(Color.TRANSPARENT);
        box.setStroke(Color.SADDLEBROWN);
        box.setStrokeWidth(2);
        Text label = new Text(x + 6, y + 16, name);
        pane.getChildren().addAll(box, label);

        for (FarmItem child : children) {
            child.draw(pane);
        }
    }

    @Override
    public void collect(List<FarmItem> out) {
        out.add(this);
        for (FarmItem child : children) {
            child.collect(out);
        }
    }

    @Override
    public String toString() { return name; }
}
