import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FarmApp extends Application {

    private Circle drone;
    private double startX, startY;       // drone's starting center
    private Animation running;           // current flight, if any

    @Override
    public void start(Stage stage) {
        // ---- Build the farm, in the table's order ----
        ItemContainer farm = new ItemContainer("Farm", 0, 0, 0, 800, 600);

        ItemContainer barn = new ItemContainer("Barn", 20000, 20, 40, 360, 300);
        ItemContainer livestock = new ItemContainer("Livestock Area", 3000, 40, 70, 200, 140);
        livestock.add(new Item("Cow", 1500, 70, 120, 50, 30));
        barn.add(livestock);
        barn.add(new Item("Milk Storage", 4000, 260, 70, 100, 60));
        farm.add(barn);

        ItemContainer storage = new ItemContainer("Storage Building", 8000, 420, 40, 360, 160);
        storage.add(new Item("Tractor", 45000, 450, 90, 100, 50));
        storage.add(new Item("Tiller", 2500, 600, 90, 80, 50));
        farm.add(storage);

        ItemContainer command = new ItemContainer("Command Center", 10000, 20, 380, 260, 180);
        Item droneItem = new Item("Drone", 1200, 120, 440, 60, 60);
        command.add(droneItem);
        farm.add(command);

        farm.add(new Item("Soy Crop", 6000, 420, 240, 360, 320));

        // ---- Pane and the one draw call ----
        Pane farmPane = new Pane();
        farmPane.setPrefSize(800, 600);
        farmPane.setMinSize(800, 600);
        farmPane.setMaxSize(800, 600);
        farm.draw(farmPane);

        // ---- Everything on the farm, from the tree ----
        List<FarmItem> all = new ArrayList<>();
        farm.collect(all);
        System.out.println("Items and containers on the farm: " + all.size());

        // ---- Drone, added last so it flies above everything ----
        startX = droneItem.getX() + droneItem.getWidth() / 2;
        startY = droneItem.getY() + droneItem.getHeight() / 2;
        drone = new Circle(startX, startY, 12, Color.DODGERBLUE);
        farmPane.getChildren().add(drone);

        // ---- Controls ----
        ComboBox<FarmItem> picker = new ComboBox<>();
        picker.getItems().addAll(all);
        picker.setPromptText("Pick an item or container");

        Button visit = new Button("Visit Item/Container");
        visit.setOnAction(e -> {
            FarmItem target = picker.getValue();
            if (target == null || isRunning()) return;
            SequentialTransition trip = new SequentialTransition(
                    flightTo(target), flightHome());
            play(trip);
        });

        Button scan = new Button("Scan Farm");
        scan.setOnAction(e -> {
            if (isRunning()) return;
            SequentialTransition tour = new SequentialTransition();
            for (FarmItem target : all.subList(1, all.size())) {   // skip root Farm
                tour.getChildren().add(flightTo(target));
            }
            tour.getChildren().add(flightHome());
            play(tour);
        });

        HBox controls = new HBox(10, picker, visit, scan);
        controls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(farmPane);
        root.setBottom(controls);

        stage.setTitle("Farm Map");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private boolean isRunning() {
        return running != null && running.getStatus() == Animation.Status.RUNNING;
    }

    private void play(Animation a) {
        running = a;
        a.play();
    }

    /** One 0.8 s flight to the center of target (relative to the drone's start). */
    private TranslateTransition flightTo(FarmItem target) {
        double toX = (target.getX() + target.getWidth() / 2) - startX;
        double toY = (target.getY() + target.getHeight() / 2) - startY;
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.8), drone);
        t.setToX(toX);
        t.setToY(toY);
        t.setOnFinished(e -> System.out.println("Drone reached: " + target.getName()));
        return t;
    }

    /** Back to where the drone was placed: translate (0, 0). */
    private TranslateTransition flightHome() {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.8), drone);
        t.setToX(0);
        t.setToY(0);
        t.setOnFinished(e -> System.out.println("Drone back at Command Center"));
        return t;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
