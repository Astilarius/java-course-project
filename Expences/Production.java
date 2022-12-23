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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Collection;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.IOException;
public class Production implements Expense {
    // public static Collection<Material> materials;
    // public static Collection<Production> productions;
    protected Collection<Expense> linked;
    static private Integer prodId = 0;
    private String elemId;
    private String name;
    private double pricePerUnit;
    private Integer amount;
    protected DragContext ctx;
    public static Stage mainStage;
    public String getString(){
        name = name.replace(" ", "_");
        String result = "2 "+name+" "+pricePerUnit+" "+amount+" "+elemId+" ";
        for (Expense e : linked){
            result += e.getId()+"|"; 
        }
        result+=";";
        return result;
    }
    // public static void setCollections(Collection<Material> newMats, Collection<Production> newProds){
    //     materials = newMats;
    //     productions = newProds;
    // }
    public void sendToServer(DataOutputStream out){
        try
        {
            System.out.println("size - "+linked.size());
            for (Expense child : linked){
                child.sendToServer(out);
                System.out.println("sent child of "+name);
            }
            out.writeUTF(getString());
            // out.writeInt(2);
            // out.writeUTF(name);
            // out.writeInt(amount);
            // out.writeDouble(pricePerUnit);
        }
        catch(IOException e) {}
    }
    public Collection<Expense> getLinked(){
        return linked;
    }
    public String getName(){
        return name;
    }
    public double getPricePerUnit(){
        return pricePerUnit;
    }
    public String getId(){
        return elemId;
    }
    public double getAmount(){
        return amount;
    }
    public Production(){
        elemId = "P"+prodId;
        prodId++;
        name = "";
        pricePerUnit = 0;
        amount = 0;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    public Production(String newName, Integer newAmount, Double newPricePerUnit){
        elemId = "P"+prodId;
        prodId++;
        name = newName;
        amount = newAmount;
        pricePerUnit = newPricePerUnit;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
    }
    public Production(String newName, Integer newAmount, Double newPricePerUnit, Collection<Expense> newLinked){
        elemId = "P"+prodId;
        prodId++;
        name = newName;
        amount = newAmount;
        pricePerUnit = newPricePerUnit;
        ctx = new DragContext();
        linked = new ArrayList<Expense>();
        for(Expense e : newLinked){
            linked.add(e);
        }
    }
    public VBox getElem(){
        VBox elem = new VBox();
        Label idL = new Label("id "+String.valueOf(elemId));
        Label l = new Label("name - "+name);
        Label l2 = new Label("amount - "+amount);
        Label l3 = new Label("price per unit - "+pricePerUnit);
        Button edit = new Button("edit");
        HBox linkButtons = new HBox();
        Button link = new Button("link");
        Button clearLinks = new Button("clear links");
        clearLinks.setOnAction(event->{
            linked.clear();
        });
        link.setOnAction(event2->{
            Stage stage2 = new Stage();
            VBox root2 = new VBox();
            ToggleGroup toggleGroup = new ToggleGroup();
            Button submitToggle = new Button("submit");
            Collection<RadioButton> buttons = new ArrayList<RadioButton>();
            // for(Material mat : materials){
            //     if (linked.contains(mat)) continue;
            //     VBox tempV = new VBox();
            //     RadioButton b1 = new RadioButton("id"+mat.getId() + " " + mat.getName());
            //     buttons.add(b1);
            //     b1.setId(String.valueOf(mat.getId()));
            //     b1.setToggleGroup(toggleGroup);
            //     tempV.getChildren().add(b1);
            //     root2.getChildren().add(tempV);
            // }
            for (Expense e : expenses){
                if (e == this) continue;
                if (linked.contains(e)) continue;
                VBox tempV = new VBox();
                RadioButton b1 = new RadioButton("id"+e.getId() + " " + e.getName());
                buttons.add(b1);
                b1.setId(String.valueOf(e.getId()));
                b1.setToggleGroup(toggleGroup);
                tempV.getChildren().add(b1);
                root2.getChildren().add(tempV);
            }
            // for(Production prod : productions){
            //     if (prod == this) continue;
            //     if (linked.contains(prod)) continue;
            //     VBox tempV = new VBox();
            //     RadioButton b1 = new RadioButton("id"+prod.getId() + " " + prod.getName());
            //     buttons.add(b1);
            //     b1.setId(String.valueOf(prod.getId()));
            //     b1.setToggleGroup(toggleGroup);
            //     tempV.getChildren().add(b1);
            //     root2.getChildren().add(tempV);
            // }
            submitToggle.setOnAction(event3->{
                for (RadioButton b : buttons){
                    if (b.isSelected()){
                        String tempId = b.getId();
                        for (Expense e : expenses){
                            if (e.getId() == tempId){
                                if (linked.contains(e)){
                                    Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                if(e.getId() == elemId){
                                    Alert error = new Alert(AlertType.ERROR, "Cannot link element with itself", ButtonType.OK);
                                    error.showAndWait();
                                    stage2.close();
                                    break;
                                }
                                linked.add(e);
                                System.out.println(linked);
                            }
                        }
                        // for (Production p : productions){
                        //     if (p.elemId == tempId){
                        //         if (linked.contains(p)){
                        //             Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                        //             error.showAndWait();
                        //             stage2.close();
                        //             break;
                        //         }
                        //         if(p.elemId == elemId){
                        //             Alert error = new Alert(AlertType.ERROR, "Cannot link element with itself", ButtonType.OK);
                        //             error.showAndWait();
                        //             stage2.close();
                        //             break;
                        //         }
                        //         linked.add(p);
                        //         System.out.println(linked);
                        //     }
                        // }
                        // for (Material m : materials){
                        //     if (m.getId() == tempId){
                        //         if (linked.contains(m)){
                        //             Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                        //             error.showAndWait();
                        //             stage2.close();
                        //             break;
                        //         }
                        //         linked.add(m);
                        //         System.out.println(linked);
                        //     }
                        // }
                    }
                }
                stage2.close();
            });
            root2.getChildren().add(submitToggle);
            stage2.setTitle("Linker");
            stage2.setScene(new Scene(root2));
            stage2.show();
        });
        edit.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#9c9c9c;-fx-border-color:#d6d6d6;");
        link.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#9c9c9c;-fx-border-color:#d6d6d6;");
        clearLinks.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#9c9c9c;-fx-border-color:#d6d6d6;");
        elem.setStyle("-fx-background-color:#d4d4d4;-fx-border-width:1px;-fx-border-color:#9c9c9c;-fx-padding:5px;-fx-border-insets:5px;-fx-background-insets:5px;-fx-border-radius:5;-fx-background-radius:5;");
        
        elem.setOnMousePressed(event->{
            ctx.mouseX = event.getSceneX();
            ctx.mouseY = event.getSceneY();
            ctx.nodeX = elem.getTranslateX();
            ctx.nodeY = elem.getTranslateY();
            ctx.screenX = elem.getScene().getWindow().getWidth();
            ctx.screenY = elem.getScene().getWindow().getHeight();
            System.out.println(ctx.screenX+" "+ctx.screenY);
        });
        elem.setOnMouseDragged(event->{
            Double newX = ctx.nodeX + event.getSceneX() - ctx.mouseX;
            Double newY = ctx.nodeY + event.getSceneY() - ctx.mouseY;
            // if( newX < -20.0 ) newX = -20.0;
            if( newY < -10.0 ) newY = -10.0;
            // if( newX > ctx.screenX-175 ) newX = ctx.screenX-175;
            // if( newY > ctx.screenY-185 ) newY = ctx.screenY-185;
            elem.setTranslateX( newX );
            elem.setTranslateY( newY );
        });
        edit.setOnAction(event->{
            edit(elem);
        });
        elem.getChildren().add(idL);
        elem.getChildren().add(l);
        elem.getChildren().add(l2);
        elem.getChildren().add(l3);
        elem.getChildren().add(edit);
        linkButtons.getChildren().add(link);
        linkButtons.getChildren().add(clearLinks);
        elem.getChildren().add(linkButtons);
        // elem.setPrefSize(elem.getHeight(), elem.getWidth());

        return elem;
    }
    public static void CreateElem(FlowPane body, Stage mainStage){
        
        VBox root = new VBox();
        Stage stage = new Stage();
        
        TextField t = new TextField("name");
        TextField t2 = new TextField("amount");
        TextField t3 = new TextField("price per unit");
        Button submit = new Button("submit");
        String buttonColor = "d4d4d4";
        String buttonBorderColor = "9c9c9c";
        submit.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#"+buttonColor+";-fx-border-color:#"+buttonBorderColor+";");

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
                // productions.add(prod);
                expenses.add(prod);
                for(Expense e : expenses){
                    System.out.println(e.getId());
                }
                VBox elem = prod.getElem();
                
                body.getChildren().add(elem);
                stage.close();
                mainStage.show();
            }
        });
        root.setStyle("-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px; -fx-border-radius: 3px;");
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