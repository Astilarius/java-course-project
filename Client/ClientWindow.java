package Client;
import Expences.Material;
import Expences.Production;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
// import javafx.scene.paint.Material;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.util.Collection;
import java.util.ArrayList;
public class ClientWindow extends Application {
    @Override
    public void start(Stage stage) {
        Collection<Material> materials = new ArrayList<Material>();
        Collection<Production> productions = new ArrayList<Production>();
        Production.setCollections(materials, productions);
        final Button materialButton = new Button("Add material");
        final Button productionButton = new Button("Add production");
        Integer currentId = 0;
        final Pane leftButtonSpacer = new Pane();
        final Pane rightButtonSpacer = new Pane();
        HBox.setHgrow(leftButtonSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightButtonSpacer, Priority.ALWAYS);
        leftButtonSpacer.setMinSize(10, 1);
        rightButtonSpacer.setMinSize(10, 1);

        HBox buttons = new HBox();
        buttons.getChildren().add(materialButton);
        buttons.getChildren().add(leftButtonSpacer);
        buttons.getChildren().add(rightButtonSpacer);
        buttons.getChildren().add(productionButton);

        FlowPane body = new FlowPane();
        body.setPadding(new Insets(10, 10, 10, 10));
        materialButton.setOnMouseClicked(event -> {
            VBox root2 = new VBox();
            Stage stage2 = new Stage();
            Material item = new Material(body, stage2, currentId);
            root2.getChildren().addAll(item.getElem());

            stage2.setTitle("Production Creator");
            stage2.setScene(new Scene(root2));
            stage2.show();
        });
        productionButton.setOnMouseClicked(event -> {
            Production.CreateElem(body, stage);
            stage.hide();
        });
        VBox root = new VBox(buttons, body);
        root.setPadding(new Insets(10, 10, 10, 10));

        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}