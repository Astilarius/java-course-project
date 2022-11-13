package Expences;

// при создании расхода на производство нужно будет указать:
// сумму, название
import Drag.DragContext;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Collection;
import java.util.ArrayList;
import javafx.scene.Node;
public class Production implements Expense {
    public static Collection<Material> materials;
    public static Collection<Production> productions;
    private Collection<Expense> linked;
    static private Integer prodId = 0;
    private Integer elemId;
    private String name;
    private double pricePerUnit;
    private DragContext ctx;
    public static void setCollections(Collection<Material> newMats, Collection<Production> newProds){
        materials = newMats;
        productions = newProds;
    }
    public String getName(){
        return name;
    }
    public double getPricePerUnit(){
        return pricePerUnit;
    }
    public Integer getId(){
        return elemId;
    }
    private Production(){
        elemId = prodId;
        prodId++;
        name = "";
        pricePerUnit = 0;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    private Production(String newName, Double newPricePerUnit){
        elemId = prodId;
        prodId++;
        name = newName;
        pricePerUnit = newPricePerUnit;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    public VBox getElem(){
        VBox elem = new VBox();
        Label idL = new Label("id "+String.valueOf(elemId));
        Label l = new Label("name - "+name);
        Label l2 = new Label("price per unit - "+pricePerUnit);
        Button edit = new Button("edit");
        Button link = new Button("link");
        link.setOnMouseClicked(event2->{
            Stage stage2 = new Stage();
            VBox root2 = new VBox();
            ToggleGroup toggleGroup = new ToggleGroup();
            Button submitToggle = new Button("submit");
            Collection<RadioButton> buttons = new ArrayList<RadioButton>();
            for(Production prod : productions){
                VBox tempV = new VBox();
                RadioButton b1 = new RadioButton("id"+prod.getId() + " " + prod.getName());
                buttons.add(b1);
                b1.setId(String.valueOf(prod.getId()));
                b1.setToggleGroup(toggleGroup);
                tempV.getChildren().add(b1);
                root2.getChildren().add(tempV);
            }
            submitToggle.setOnMouseClicked(event3->{
                for (RadioButton b : buttons){
                    if (b.isSelected()){
                        Integer tempId = Integer.parseInt(b.getId());
                        for (Production p : productions){
                            if (p.getId() == tempId){
                                if (linked.contains(p)){
                                    Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                linked.add(p);
                                System.out.println(linked);
                            }
                        }
                    }
                }
                stage2.close();
            });
            root2.getChildren().add(submitToggle);
            stage2.setTitle("Linker");
            stage2.setScene(new Scene(root2));
            stage2.show();
        });
        elem.setStyle("-fx-border-color:black;-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
        
        elem.setOnMousePressed(event->{
            ctx.mouseX = event.getX();
            ctx.mouseY = event.getY();
            ctx.nodeX = elem.getTranslateX();
            ctx.nodeY = elem.getTranslateY();
        });
        elem.setOnMouseDragged(event->{
            elem.setTranslateX(ctx.nodeX + event.getX() - ctx.mouseX);
            elem.setTranslateY(ctx.nodeY + event.getY() - ctx.mouseY);
        });
        edit.setOnMouseClicked(event->{
            elem.getChildren().clear();
            elem.getChildren().setAll(edit(elem));
        });
        elem.getChildren().add(idL);
        elem.getChildren().add(l);
        elem.getChildren().add(l2);
        elem.getChildren().add(edit);
        elem.getChildren().add(link);

        return elem;
    }
    public static void CreateElem(FlowPane body, Stage mainStage){
        
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField("name");
        TextField t2 = new TextField("price per unit");
        Button submit = new Button("submit");
        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            if(!text2.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("WRONG");
            } else {
                Production prod = new Production(text, Double.parseDouble(text2));
                productions.add(prod);
                VBox elem = prod.getElem();
                
                body.getChildren().add(elem);
                stage.close();
                mainStage.show();
            }
        });
        root.setStyle("-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
        root.getChildren().add(t);
        root.getChildren().add(t2);
        root.getChildren().add(submit);
        root.getChildren().add(msg);

        stage.setTitle("Production Creator");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event->{
            mainStage.show();
        });
        stage.show();
    }
    public Node[] edit(VBox elem){
        Node[] nodes = new Node[4];
        
        TextField t = new TextField(name);
        TextField t2 = new TextField(String.valueOf(pricePerUnit));
        Button submit = new Button("submit");
        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            if(!text2.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("WRONG");
            } else {
                name = text;
                pricePerUnit = Double.parseDouble(text2);
                elem.getChildren().clear();
                Label idL = new Label("id "+String.valueOf(elemId));
                Label l = new Label("name - "+name);
                Label l2 = new Label("price per unit - "+pricePerUnit);
                Button edit = new Button("edit");
                Button link = new Button("link");
                link.setOnMouseClicked(event2->{
                    Stage stage2 = new Stage();
                    VBox root2 = new VBox();
                    ToggleGroup toggleGroup = new ToggleGroup();
                    Button submitToggle = new Button("submit");
                    Collection<RadioButton> buttons = new ArrayList<RadioButton>();
                    for(Production prod : productions){
                        VBox tempV = new VBox();
                        RadioButton b1 = new RadioButton("id"+prod.getId() + " " + prod.getName());
                        buttons.add(b1);
                        b1.setId(String.valueOf(prod.getId()));
                        b1.setToggleGroup(toggleGroup);
                        tempV.getChildren().add(b1);
                        root2.getChildren().add(tempV);
                    }
                    submitToggle.setOnMouseClicked(event3->{
                        for (RadioButton b : buttons){
                            if (b.isSelected()){
                                Integer tempId = Integer.parseInt(b.getId());
                                for (Production p : productions){
                                    if (p.getId() == tempId){
                                        linked.add(p);
                                        System.out.println(linked);
                                    }
                                }
                            }
                        }
                        stage2.close();
                    });
                    root2.getChildren().add(submitToggle);
                    stage2.setTitle("Linker");
                    stage2.setScene(new Scene(root2));
                    stage2.show();
                });
                edit.setOnMouseClicked(event2->{
                    elem.getChildren().clear();
                    elem.getChildren().setAll(edit(elem));
                });
                elem.getChildren().setAll(idL, l , l2, edit, link);
            }
        });
        nodes[0] = t;
        nodes[1] = t2;
        nodes[2] = submit;
        nodes[3] = msg;
        return nodes;
    }
}