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
public class Material implements Expense {
    private Integer id;
    private String name;
    private double price;
    private int amount;
    private VBox elem;
    private DragContext ctx;
    public Material(FlowPane body, Stage newStage, Integer newId){
        id = newId;
        name = "";
        price = 0;
        amount = 0;

        ctx = new DragContext();
        VBox tempElem = new VBox();
        // Double mousex = 0.0;
        // Double mousey = 0.0;
        
        TextField t = new TextField("name");
        TextField t2 = new TextField("price");
        TextField t3 = new TextField("amount");
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
            if( (!text2.matches("^[\\d]+(\\.[\\d]+)?$")) || (!text3.matches("^[\\d]+$"))){
                msg.setText("WRONG");
            } else {
                name = text;
                price = Double.parseDouble(text2);
                amount = Integer.parseInt(text3);
                Label idL = new Label("id "+String.valueOf(id));
                Label l = new Label("name - "+name);
                Label l2 = new Label("price per unit - "+price);
                Label l3 = new Label("amount - "+amount);
                elem = new VBox();
                elem.setStyle("-fx-border-color:black;-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
                
                elem.setOnMousePressed(event2->{
                    ctx.mouseX = event2.getX();
                    ctx.mouseY = event2.getY();
                    ctx.nodeX = tempElem.getTranslateX();
                    ctx.nodeY = tempElem.getTranslateY();
                });
                elem.setOnMouseDragged(event2->{
                    tempElem.setTranslateX(ctx.nodeX + event2.getX() - ctx.mouseX);
                    tempElem.setTranslateY(ctx.nodeY + event2.getY() - ctx.mouseY);
                });
                elem.getChildren().add(idL);
                elem.getChildren().add(l);
                elem.getChildren().add(l2);
                elem.getChildren().add(l3);
                body.getChildren().add(elem);
                newStage.close();
            }
        });
        tempElem.setStyle("-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
        tempElem.getChildren().add(t);
        tempElem.getChildren().add(t2);
        tempElem.getChildren().add(t3);
        tempElem.getChildren().add(submit);
        tempElem.getChildren().add(msg);
        elem = tempElem;


        // VBox elem = new VBox();
        // TextField t = new TextField("name");
        // TextField t2 = new TextField("price");
        // TextField t3 = new TextField("amount");
        // Button submit = new Button("submit");
        // Label msg = new Label();
        // t.setOnKeyPressed(event->{
        //     KeyCode code = event.getCode();
        //     if (code.name() == "ENTER"){
        //         submit.fire();
        //     }
        // });
        // t2.setOnKeyPressed(event->{
        //     KeyCode code = event.getCode();
        //     if (code.name() == "ENTER"){
        //         submit.fire();
        //     }
        // });
        // t3.setOnKeyPressed(event->{
        //     KeyCode code = event.getCode();
        //     if (code.name() == "ENTER"){
        //         submit.fire();
        //     }
        // });
        // submit.setOnAction(event -> {
        //     String text = t.getText();
        //     String text2 = t2.getText();
        //     String text3 = t3.getText();
        //     if( (!text2.matches("^[\\d]+(\\.[\\d]+)?$")) || (!text3.matches("^[\\d]+(\\.[\\d]+)?$")) ){
        //         msg.setText("WRONG");
        //     } else {
        //         Label l = new Label("name - "+t.getText());
        //         Label l2 = new Label("price - "+t2.getText());
        //         Label l3 = new Label("amount - "+t3.getText());
        //         elem.getChildren().remove(msg);
        //         elem.getChildren().remove(t);
        //         elem.getChildren().remove(t2);
        //         elem.getChildren().remove(t3);
        //         elem.getChildren().remove(submit);
        //         elem.getChildren().add(l);
        //         elem.getChildren().add(l2);
        //         elem.getChildren().add(l3);
        //     }
        // });
        // elem.setStyle("-fx-border-color:black;-fx-padding: 5px;-fx-border-insets: 5px;-fx-background-insets: 5px;");
        // elem.getChildren().add(t);
        // elem.getChildren().add(t2);
        // elem.getChildren().add(t3);
        // elem.getChildren().add(submit);
        // elem.getChildren().add(msg);
    }
    public VBox getElem(){
        return elem;
    }
    public void edit(){}
}
