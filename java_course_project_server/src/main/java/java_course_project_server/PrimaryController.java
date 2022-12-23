package java_course_project_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import com.google.gson.GsonBuilder;

import Models.Account;
import Models.Expense;
import Models.ExpenseDeserializer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PrimaryController {
    public void initialize(){
        Thread t1=new Thread(new Runnable()
        {
            
        public void run()
            {
                try {
        
                    // server is listening on port 1234
                    App.server = new ServerSocket(12345);
                    App.server.setReuseAddress(true);
                    while (true) {
                        // socket object to receive incoming client
                        // requests
                        Socket client = App.server.accept();
        
                        // Displaying that new client is connected
                        // to server
                        System.out.println("New client connected "
                                        + client.getInetAddress()
                                                .getHostAddress());
        
                        // create a new thread object
                        ClientHandler clientSock = new ClientHandler(client);
        
                        // This thread will handle the client
                        // separately
                        new Thread(clientSock).start();
                    }
                    
                }
                catch(IOException e) {}//кстати обязательно ловим эту ошибку
            }
        });
        t1.start();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    VBox accBox;

    @FXML
    public void showAccounts(){
        Collection<Account> accs = DatabaseController.getAccounts();
        accBox.getChildren().clear();
        for(Account acc : accs){
            HBox h = new HBox();
            Label l = new Label("ID= "+acc.getId()+" имя аккаунта="+acc.getAccountName()+" подтвержден=");
            CheckBox cb = new CheckBox();
            cb.setId(acc.getAccountName());
            if(acc.isVerified()){
                cb.fire();
            }
            cb.setOnAction(event->{
                if(cb.isSelected()){
                    DatabaseController.verifyAccount(cb.getId(), true);
                } else {
                    DatabaseController.verifyAccount(cb.getId(), false);
                }
            });
            h.getChildren().addAll(l, cb);
            accBox.getChildren().add(h);
        }
    }
}
