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
    
    public static void manageServer(FlowPane body, Stage mainStage){
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
                    processDataString(tempS2, body);
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

    private static void processDataString(String dataString, FlowPane body){
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
                String[] linkedString = prodString[5].split("\\|");

                // for(String test1 : prodString){
                //     System.out.println("test1 "+test1);
                // }
                // for(String test2 : linkedString){
                //     System.out.println("test2 "+test2);
                // }

                for(String linkedElemString : linkedString){
                    System.out.println("linkedElemString "+linkedElemString);
                    if (linkedElemString.startsWith("M")){
                        System.out.println("materials.size "+materials.size());
                        materials.forEach(linkedMat->{
                            System.out.println(linkedElemString+" == "+linkedMat.getId()+" = "+linkedMat.getId() == linkedElemString);
                            if(linkedMat.getId() == linkedElemString){
                                tempLinked.add(linkedMat);
                            }
                        });
                    } else if (linkedElemString.startsWith("P")){
                        System.out.println("productions.size "+productions.size());
                        productions.forEach(linkedProd->{
                            System.out.println(linkedElemString+" == "+linkedProd.getId()+" = "+linkedProd.getId() == linkedElemString);
                            if(linkedProd.getId() == linkedElemString){
                                tempLinked.add(linkedProd);
                            }
                        });
                    }
                }
                Production tempP = new Production(prodString[1], Integer.parseInt(prodString[3]), Double.parseDouble(prodString[2]), tempLinked);
                System.out.println("temp linked - "+tempLinked.size());
                System.out.println("linked - "+tempP.getLinked().size());
                tempLinked.clear();
                body.getChildren().add(tempP.getElem());
                productions.add(tempP);
            }
        }
        ProdResult finalRes = new ProdResult();
        body.getChildren().add(finalRes.getProdRes());                        
        Expense.expenses.clear();
        for(Material m : materials){
            Expense.expenses.add(m);
        }
        for(Production p : productions){
            Expense.expenses.add(p);
        }
    }
}
