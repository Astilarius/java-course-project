package Expences;

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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
public class ProdResult extends Production{
    private String name = "result";
    private double pricePerUnit = 1;
    public VBox getProdRes(){
        VBox elem = new VBox();
        Label l = new Label("result");
        Button link = new Button("link");
        Button calcRes = new Button("Calculate result");
        calcRes.setOnMouseClicked(event->{
            Socket sock = null; 
            if(linked.size() > 0){
                try
                {
                    System.out.println("Connecting to server...");
                    sock = new Socket("localhost",12345);
                    System.out.println("Connected");
                    DataInputStream in = new DataInputStream(sock.getInputStream());
                    DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                    sendToServer(out);
                    out.writeInt(0);
                }
                catch(IOException e) {
                    Alert error = new Alert(AlertType.ERROR, "Error while connecting to server: "+e.getMessage(), ButtonType.OK);
                    error.showAndWait();
                }
            } else {
                Alert error = new Alert(AlertType.ERROR, "You need to link this element with production to calculate result", ButtonType.OK);
                error.showAndWait();
            }
        });
        link.setOnMouseClicked(event2->{
            if (productions.size() > 0){
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
            } else {
                Alert error = new Alert(AlertType.ERROR, "Nothing to link with", ButtonType.OK);
                error.showAndWait();
            }
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
        elem.getChildren().add(l);
        elem.getChildren().add(link);
        elem.getChildren().add(calcRes);

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
