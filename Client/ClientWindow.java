package Client;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
public class ClientWindow extends Application {
    @Override
    public void start(Stage stage) {
        
    HBox root = new HBox();
    root.setPadding(new Insets(10, 10, 10, 10));

    final Button left = new Button("Left");
    final Button center = new Button("Center");
    final Button right = new Button("Right");
    VBox leftBox = new VBox();
    VBox centerBox = new VBox();
    VBox rightBox = new VBox();
    center.setOnMouseClicked(event -> {
        Label l = new Label("Ы");
        leftBox.getChildren().add(l);
    });

    final Pane leftSpacer = new Pane();
    final Pane rightSpacer = new Pane();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);
    leftSpacer.setMinSize(10, 1);
    rightSpacer.setMinSize(10, 1);

    root.getChildren().addAll(leftBox, leftSpacer, center, rightSpacer, rightBox);

    stage.setScene(new Scene(root, 400, 400));
    stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}