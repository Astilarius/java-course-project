package Models;

import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Collection;
import java.util.ArrayList;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
public class ProdResult extends Production{
    // private String name = "result";
    // private double pricePerUnit = 1;
    public static Collection<Expense> linked = new ArrayList<Expense>();
    public VBox getProdRes(){
        VBox elem = new VBox();
        Label l = new Label("результат");
        Button link = new Button("Связать");
        link.setOnMouseClicked(event2->{
            if (expenses.size() > 0){
                Stage stage2 = new Stage();
                VBox root2 = new VBox();
                ToggleGroup toggleGroup = new ToggleGroup();
                Button submitToggle = new Button("Подтвердить");
                Collection<RadioButton> buttons = new ArrayList<RadioButton>();
                for (Expense e : expenses){
                    if (e.getClass() == Material.class) continue;
                    VBox tempV = new VBox();
                    RadioButton b1 = new RadioButton("id"+e.getId() + " " + e.getName());
                    buttons.add(b1);
                    b1.setId(String.valueOf(e.getId()));
                    b1.setToggleGroup(toggleGroup);
                    tempV.getChildren().add(b1);
                    root2.getChildren().add(tempV);
                }
                submitToggle.setOnMouseClicked(event3->{
                    linked.clear();
                    for (RadioButton b : buttons){
                        if (b.isSelected()){
                            String tempId = b.getId();
                            for (Expense e : expenses){
                                if (e.getId() == tempId){
                                    linked.add(e);
                                    System.out.println(linked);
                                }
                            }
                            // for (Production p : productions){
                            //     if (p.getId() == tempId){
                            //         if (linked.contains(p)){
                            //             Alert error = new Alert(AlertType.ERROR, "Already linked", ButtonType.OK);
                            //             error.showAndWait();
                            //             stage2.close();
                            //             break;
                            //         }
                            //         linked.add(p);
                            //         System.out.println(linked);
                            //     }
                            // }
                        }
                    }
                    stage2.close();
                });
                root2.getChildren().add(submitToggle);
                stage2.setTitle("Связывание");
                stage2.setScene(new Scene(root2));
                stage2.show();
            } else {
                Alert error = new Alert(AlertType.ERROR, "Не с чем связывать", ButtonType.OK);
                error.showAndWait();
            }
        });
        link.setStyle("-fx-border-radius:25;-fx-background-radius:25;-fx-background-color:#9c9c9c;-fx-border-color:#d6d6d6;");
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
            if( newX < -20.0 ) newX = -20.0;
            if( newY < -10.0 ) newY = -10.0;
            if( newX > ctx.screenX-175 ) newX = ctx.screenX-175;
            if( newY > ctx.screenY-185 ) newY = ctx.screenY-185;
            elem.setTranslateX( newX );
            elem.setTranslateY( newY );
        });
        elem.getChildren().add(l);
        elem.getChildren().add(link);

        return elem;
    }
    // private void sendExpences(DataOutputStream out, Expense sendMe){
    //     if (sendMe.getClass() == Production.class){
    //     }
    //     if (sendMe.getClass() == Material.class){
    //         sendMe.sendToServer(out);
    //     }
    // }
}
