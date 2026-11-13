import java.util.List;
import javafx.scene.layout.Pane;

public interface FarmItem {
    String getName();

    /** Price of this object plus everything inside it. */
    double getPrice();

    double getX();
    double getY();
    double getWidth();
    double getHeight();

    /** Adds this object's shapes, and those of everything inside it, to the pane. */
    void draw(Pane pane);

    /** Adds this object, then everything inside it, to out, in tree order. */
    void collect(List<FarmItem> out);
}
