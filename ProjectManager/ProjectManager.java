package ProjectManager;

import Expences.ProdResult;
import Expences.Expense;
import Expences.Material;
import Expences.Production;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.Collection;

public class ProjectManager {
    
    public static void manageServer(FlowPane body, Stage mainStage, Collection<Material> realM, Collection<Production> realP){
        VBox root = new VBox();
        Stage stage = new Stage();


        Socket sock = null; 
        try
        {
            System.out.println("Connecting to server...");
            sock = new Socket("localhost",12345);
            System.out.println("Connected");
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            out.writeUTF("manage");
            System.out.println("manage");
            Integer tempI = 0;
            Double tempD = 0.0;
            String tempS = "";
            while(true){
                tempI = in.readInt();
                final Integer id = tempI;
                System.out.println(tempI);
                if (tempI == -1) break;
                tempS = in.readUTF();
                System.out.println(tempS);
                tempD = in.readDouble();
                System.out.println(tempD);
                String tempS2 = in.readUTF();
                System.out.println(tempS2);
                final Label label = new Label("шв - "+tempI+", name - "+tempS+" price - "+tempD );
                final Button open = new Button("open");
                open.setOnAction(event->{
                    processDataString(tempS2, body, realM, realP);
                    mainStage.show();
                    stage.close();
                });
                final Button delete = new Button("delete");
                delete.setOnAction(event->{
                    try {
                        Socket sock2 = null; 
                        try {
                        System.out.println("Connecting to server...");
                        sock2 = new Socket("localhost",12345);
                        System.out.println("Connected");
                        DataOutputStream out2 = new DataOutputStream(sock2.getOutputStream());
                        System.out.println("delete");
                        out2.writeUTF("delete "+id);
                        } catch (Exception e) {
                            Alert error = new Alert(AlertType.ERROR, "Error while connecting to server: "+e.getMessage(), ButtonType.OK);
                            error.showAndWait();
                        } finally {
                            sock2.close();
                        }
                    } catch (Exception e) {
                        Alert error = new Alert(AlertType.ERROR, "Error while connecting to server: "+e.getMessage(), ButtonType.OK);
                        error.showAndWait();
                    }
                });
                final Button addAsElem = new Button("add as element");
                addAsElem.setOnAction(event->{
                    
                });
                final HBox hbox = new HBox();
                hbox.getChildren().addAll(label, open, delete, addAsElem);
                root.getChildren().addAll(hbox);
            }
            out.writeUTF("end");
            stage.setTitle("Production Creator");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event->{
                mainStage.show();
            });
            stage.setResizable(false);
            stage.show();
        }
        catch(IOException e) {
            Alert error = new Alert(AlertType.ERROR, "Error while connecting to server: "+e.getMessage(), ButtonType.OK);
            error.showAndWait();
        }
    }

    private static void processDataString(String dataString, FlowPane body, Collection<Material> realM, Collection<Production> realP){
        Collection<Material> materials = new ArrayList<Material>();
        Collection<Production> productions = new ArrayList<Production>();
        Collection<Expense> tempLinked = new ArrayList<Expense>();
        String[] splittedDataString = dataString.split(";");
        body.getChildren().clear();

        for(String elem : splittedDataString){
            if (elem.startsWith("1")){
                String[] matString = elem.split(" ");
                Material tempM = new Material(matString[1], Double.parseDouble(matString[2]), Integer.parseInt(matString[3]));
                body.getChildren().add(tempM.getElem());
                materials.add(tempM);
            } else if (elem.startsWith("2")){
                String[] prodString = elem.split(" ");
                String[] linkedString = prodString[4].split("\\|");
                for(String linkedElemString : linkedString){
                    if (linkedElemString.startsWith("M")){
                        materials.forEach(linkedMat->{
                            if(linkedMat.getId() == linkedElemString){
                                tempLinked.add(linkedMat);
                            }
                        });
                    } else if (linkedElemString.startsWith("P")){
                        productions.forEach(linkedProd->{
                            if(linkedProd.getId() == linkedElemString){
                                tempLinked.add(linkedProd);
                            }
                        });
                    }
                }
                Production tempM = new Production(prodString[1], Integer.parseInt(prodString[3]), Double.parseDouble(prodString[2]), tempLinked);
                tempLinked.clear();
                body.getChildren().add(tempM.getElem());
                productions.add(tempM);
            }
        }
        ProdResult finalRes = new ProdResult();
        body.getChildren().add(finalRes.getProdRes());
        realM = materials;
        realP = productions;
    }
}
