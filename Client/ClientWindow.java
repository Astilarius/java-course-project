package Client;
import ProjectManager.ProjectManager;
import Expences.Material;
import Expences.Production;
import Expences.ProdResult;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
// import javafx.scene.paint.Material;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.util.Collection;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
public class ClientWindow extends Application {
    private VBox root = new VBox();
    private FlowPane body = new FlowPane();;
    @Override
    public void start(Stage stage) {
        Collection<Material> materials = new ArrayList<Material>();
        Collection<Production> productions = new ArrayList<Production>();
        Material.mainStage = stage;
        Production.mainStage = stage;
        Production.setCollections(materials, productions);
        Material.setCollections(materials, productions);
        final Button loadProject = new Button("load project");
        loadProject.setOnAction(event->{
            ProjectManager.manageServer(body, stage, materials, productions);
            stage.hide();
        });
        final Button materialButton = new Button("Add material");
        materialButton.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                materialButton.fire();
            }
        });
        final Button productionButton = new Button("Add production");
        productionButton.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                productionButton.fire();
            }
        });
        String buttonColor = "d4d4d4";
        String buttonBorderColor = "9c9c9c";
        loadProject.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#"+buttonColor+";-fx-border-color:#"+buttonBorderColor+";");
        materialButton.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#"+buttonColor+";-fx-border-color:#"+buttonBorderColor+";");
        productionButton.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#"+buttonColor+";-fx-border-color:#"+buttonBorderColor+";");
        
        final Pane leftButtonSpacer = new Pane();
        final Pane rightButtonSpacer = new Pane();
        HBox.setHgrow(leftButtonSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightButtonSpacer, Priority.ALWAYS);
        leftButtonSpacer.setMinSize(10, 1);
        rightButtonSpacer.setMinSize(10, 1);

        HBox buttons = new HBox();
        buttons.getChildren().add(loadProject);
        buttons.getChildren().add(leftButtonSpacer);
        buttons.getChildren().add(rightButtonSpacer);
        buttons.getChildren().add(materialButton);
        buttons.getChildren().add(productionButton);

        body = new FlowPane();
        ProdResult finalRes = new ProdResult();
        body.getChildren().add(finalRes.getProdRes());
        body.setPadding(new Insets(10, 10, 10, 10));
        materialButton.setOnAction(event -> {
            Material.CreateElem(body, stage);
            stage.hide();
        });
        productionButton.setOnAction(event -> {
            Production.CreateElem(body, stage);
            stage.hide();
        });
        root = new VBox(buttons, body);
        root.setPadding(new Insets(10, 10, 10, 10));

        stage.setScene(new Scene(root, 700, 500));
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    public void populate(String dataString) {
        body.getChildren();
    }
}