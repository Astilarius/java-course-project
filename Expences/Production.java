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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Collection;
import java.util.ArrayList;
import javafx.scene.Node;
import java.io.DataOutputStream;
import java.io.IOException;
public class Production implements Expense {
    public static Collection<Material> materials;
    public static Collection<Production> productions;
    protected Collection<Expense> linked;
    static private Integer prodId = 0;
    private Integer elemId;
    private String name;
    private double pricePerUnit;
    private Integer amount;
    protected DragContext ctx;
    public static Stage mainStage;
    public static void setCollections(Collection<Material> newMats, Collection<Production> newProds){
        materials = newMats;
        productions = newProds;
    }
    public void sendToServer(DataOutputStream out){
        try
        {
            System.out.println("size - "+linked.size());
            for (Expense child : linked){
                child.sendToServer(out);
                System.out.println("sent child of "+name);
            }
            out.writeInt(2);
            out.writeUTF(name);
            out.writeInt(amount);
            out.writeDouble(pricePerUnit);
        }
        catch(IOException e) {}
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
    public double getAmount(){
        return amount;
    }
    public Production(){
        elemId = prodId;
        prodId++;
        name = "";
        pricePerUnit = 0;
        amount = 0;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    private Production(String newName, Integer newAmount, Double newPricePerUnit){
        elemId = prodId;
        prodId++;
        name = newName;
        amount = newAmount;
        pricePerUnit = newPricePerUnit;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    public VBox getElem(){
        VBox elem = new VBox();
        Label idL = new Label("id "+String.valueOf(elemId));
        Label l = new Label("name - "+name);
        Label l2 = new Label("amount - "+amount);
        Label l3 = new Label("price per unit - "+pricePerUnit);
        Button edit = new Button("edit");
        Button link = new Button("link");
        link.setOnAction(event2->{
            Stage stage2 = new Stage();
            VBox root2 = new VBox();
            ToggleGroup toggleGroup = new ToggleGroup();
            Button submitToggle = new Button("submit");
            Collection<RadioButton> buttons = new ArrayList<RadioButton>();
            for(Material mat : materials){
                VBox tempV = new VBox();
                RadioButton b1 = new RadioButton("id"+mat.getId() + " " + mat.getName());
                buttons.add(b1);
                b1.setId(String.valueOf(mat.getId()));
                b1.setToggleGroup(toggleGroup);
                tempV.getChildren().add(b1);
                root2.getChildren().add(tempV);
            }
            for(Production prod : productions){
                VBox tempV = new VBox();
                RadioButton b1 = new RadioButton("id"+prod.getId() + " " + prod.getName());
                buttons.add(b1);
                b1.setId(String.valueOf(prod.getId()));
                b1.setToggleGroup(toggleGroup);
                tempV.getChildren().add(b1);
                root2.getChildren().add(tempV);
            }
            submitToggle.setOnAction(event3->{
                for (RadioButton b : buttons){
                    if (b.isSelected()){
                        Integer tempId = Integer.parseInt(b.getId());
                        for (Production p : productions){
                            if (p.elemId == tempId){
                                if (linked.contains(p)){
                                    Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                if(p.elemId == elemId){
                                    Alert error = new Alert(AlertType.ERROR, "Cannot link element with itself", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                linked.add(p);
                                System.out.println(linked);
                            }
                        }
                        for (Material m : materials){
                            if (m.getId() == tempId){
                                if (linked.contains(m)){
                                    Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                linked.add(m);
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
            edit(elem);
            // elem.getChildren().clear();
            // elem.getChildren().setAll(edit(elem));
        });
        elem.getChildren().add(idL);
        elem.getChildren().add(l);
        elem.getChildren().add(l2);
        elem.getChildren().add(l3);
        elem.getChildren().add(edit);
        elem.getChildren().add(link);

        return elem;
    }
    public static void CreateElem(FlowPane body, Stage mainStage){
        
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField("name");
        TextField t2 = new TextField("amount");
        TextField t3 = new TextField("price per unit");
        Button submit = new Button("submit");
        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t2.requestFocus();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t3.requestFocus();
            }
        });
        t3.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            String text3 = t3.getText();
            if(!text2.matches("^[\\d]+$") || !text3.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("WRONG");
            } else {
                Production prod = new Production(text, Integer.parseInt(text2), Double.parseDouble(text3));
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
        root.getChildren().add(t3);
        root.getChildren().add(submit);
        root.getChildren().add(msg);

        stage.setTitle("Production Creator");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event->{
            mainStage.show();
        });
        stage.setResizable(false);
        stage.show();
    }
    public void edit(VBox elem){
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField(name);
        TextField t2 = new TextField(String.valueOf(amount));
        TextField t3 = new TextField(String.valueOf(pricePerUnit));
        Button submit = new Button("submit");
        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t2.requestFocus();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t3.requestFocus();
            }
        });
        t3.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            String text3 = t3.getText();
            if(!text2.matches("^[\\d]+$") || !text3.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("WRONG");
            } else {
                name = text;
                amount = Integer.parseInt(text2);
                pricePerUnit = Double.parseDouble(text3);
                elem.getChildren().setAll(getElem().getChildren());
                mainStage.show();
                stage.close();
            }
        });

        root.getChildren().setAll(t, t2, t3, submit, msg);
        stage.setTitle("Production Editor");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event->{
            mainStage.show();
        });
        mainStage.hide();
        stage.setResizable(false);
        stage.show();
    }
}