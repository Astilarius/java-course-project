package Expences;
// при создании расхода на материалы нужно будет указать:
// цена, количество, название
import Drag.DragContext;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Collection;
import javafx.scene.Node;
import java.io.DataOutputStream;
import java.io.IOException;
public class Material implements Expense {
    private String name;
    private double price;
    private int amount;
    public static Collection<Material> materials;
    public static Collection<Production> productions;
    static private Integer prodId = 0;
    private Integer elemId;
    protected DragContext ctx;
    private VBox element;
    public static Stage mainStage;
    public static void setCollections(Collection<Material> newMats, Collection<Production> newProds){
        materials = newMats;
        productions = newProds;
    }
    public void sendToServer(DataOutputStream out){
        try
        {
            out.writeInt(1);
            out.writeUTF(name);
            out.writeDouble(price);
            out.writeInt(amount);
        }
        catch(IOException e) {}
    }
    public String getName(){
        return name;
    }
    public double getAmount(){
        return amount;
    }
    public double getPrice(){
        return price;
    }
    public Integer getId(){
        return elemId;
    }
    public Material(){
        elemId = prodId;
        prodId++;
        name = "";
        price = 0;
        amount = 0;
        ctx = new DragContext();
    }
    private Material(String newName, Double newPrice, int newAmount){
        elemId = prodId;
        prodId++;
        name = newName;
        price = newPrice;
        amount = newAmount;
        ctx = new DragContext();
    }
    public VBox getElem(){
        VBox elem = new VBox();
        Label idL = new Label("id "+String.valueOf(elemId));
        Label l = new Label("name - "+name);
        Label l2 = new Label("price - "+price);
        Label l3 = new Label("amount - "+amount);
        Button edit = new Button("edit");
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

        return elem;
    }
    public static void CreateElem(FlowPane body, Stage mainStage){
        
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField("name");
        TextField t2 = new TextField("price");
        TextField t3 = new TextField("amount");
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
            if(!text2.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("WRONG");
            } else {
                Material prod = new Material(text, Double.parseDouble(text2), Integer.parseInt(text3));
                materials.add(prod);
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
    private void edit(VBox elem){
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField(name);
        TextField t2 = new TextField(String.valueOf(price));
        TextField t3 = new TextField(String.valueOf(amount));
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
            if((!text2.matches("^[\\d]+(\\.[\\d]+)?$")) || (!text3.matches("^[\\d]+$"))){
                msg.setText("WRONG");
            } else {
                name = text;
                price = Double.parseDouble(text2);
                amount = Integer.parseInt(text3);
                elem.getChildren().setAll(getElem().getChildren());
                mainStage.show();
                stage.close();
            }
        });

        root.getChildren().setAll(t, t2, t3, submit, msg);
        stage.setTitle("Material Editor");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event->{
            mainStage.show();
        });
        mainStage.hide();
        stage.setResizable(false);
        stage.show();
    }
}
