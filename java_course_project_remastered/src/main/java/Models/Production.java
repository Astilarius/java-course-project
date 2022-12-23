package Models;

import java.util.ArrayList;
import java.util.Collection;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
// при создании расхода на производство нужно будет указать:
// сумму, название
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class Production implements Expense {
    public final int type = 2;
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
    public int getType(){return type;};
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
    public void write(FlowPane fp){
        for (Expense child : linked){
            child.write(fp);
        }
        fp.getChildren().addAll(this.getElem());
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
        Label l = new Label("Имя - "+name);
        Label l2 = new Label("Количество - "+amount);
        Label l3 = new Label("Цена - "+pricePerUnit);
        Button edit = new Button("Изменить");
        HBox linkButtons = new HBox();
        Button link = new Button("Связать");
        Button clearLinks = new Button("Убрать связи");
        clearLinks.setOnAction(event->{
            linked.clear();
        });
        link.setOnAction(event2->{
            Stage stage2 = new Stage();
            VBox root2 = new VBox();
            ToggleGroup toggleGroup = new ToggleGroup();
            Button submitToggle = new Button("Подтвердить");
            Collection<RadioButton> buttons = new ArrayList<RadioButton>();
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
            if(buttons.isEmpty()){
                Alert error = new Alert(AlertType.ERROR, "Не с чем связывать", ButtonType.OK);
                error.showAndWait();
            } else {
                submitToggle.setOnAction(event3->{
                    for (RadioButton b : buttons){
                        if (b.isSelected()){
                            String tempId = b.getId();
                            for (Expense e : expenses){
                                if (e.getId() == tempId){
                                    if (linked.contains(e)){
                                        Alert error = new Alert(AlertType.ERROR, "Уже связан", ButtonType.OK);
                                        error.showAndWait();
                                        stage2.close();
                                        break;
                                    }
                                    if(e.getId() == elemId){
                                        Alert error = new Alert(AlertType.ERROR, "Нельзя связать элемент с самим собой", ButtonType.OK);
                                        error.showAndWait();
                                        stage2.close();
                                        break;
                                    }
                                    linked.add(e);
                                    System.out.println(linked);
                                }
                            }
                        }
                    }
                    stage2.close();
                });
                root2.getChildren().add(submitToggle);
                stage2.setTitle("Связывание элементов");
                stage2.setScene(new Scene(root2));
                stage2.show();
            }
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
        elem.getChildren().add(l3);
        elem.getChildren().add(l2);
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
        
        TextField t = new TextField("Имя");
        TextField t2 = new TextField("Количество");
        TextField t3 = new TextField("Цена");
        Button submit = new Button("Подтвердить");
        String buttonColor = "d4d4d4";
        String buttonBorderColor = "9c9c9c";
        submit.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#"+buttonColor+";-fx-border-color:#"+buttonBorderColor+";");

        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t3.requestFocus();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        t3.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t2.requestFocus();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            String text3 = t3.getText();
            if(!text2.matches("^[\\d]+$") || !text3.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("Цена или количество введены неправильно");
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
        root.getChildren().add(t3);
        root.getChildren().add(t2);
        root.getChildren().add(submit);
        root.getChildren().add(msg);

        stage.setTitle("Созданеи производства");
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
        Button submit = new Button("Подтвердить");
        Label msg = new Label();
        t.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t3.requestFocus();
            }
        });
        t2.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                submit.fire();
            }
        });
        t3.setOnKeyPressed(event->{
            KeyCode code = event.getCode();
            if (code.name() == "ENTER"){
                t2.requestFocus();
            }
        });
        submit.setOnAction(event -> {
            String text = t.getText();
            String text2 = t2.getText();
            String text3 = t3.getText();
            if(!text2.matches("^[\\d]+$") || !text3.matches("^[\\d]+(\\.[\\d]+)?$")){
                msg.setText("Цена или количество введены неправильно");
            } else {
                name = text;
                amount = Integer.parseInt(text2);
                pricePerUnit = Double.parseDouble(text3);
                elem.getChildren().setAll(getElem().getChildren());
                // mainStage.show();
                stage.close();
            }
        });

        root.getChildren().setAll(t, t3, t2, submit, msg);
        stage.setTitle("Изменение производства");
        stage.setScene(new Scene(root));
        // stage.setOnCloseRequest(event->{
        //     mainStage.show();
        // });
        // mainStage.hide();
        stage.setResizable(false);
        stage.show();
    }
}