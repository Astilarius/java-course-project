package java_course_project_remastered;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Models.Account;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;


public class PrimaryController {
    public void initialize(){
        try{
            App.sock = new Socket("localhost",12345);
            App.in = new DataInputStream(App.sock.getInputStream());
            App.out = new DataOutputStream(App.sock.getOutputStream());
        } catch (Exception e){
            e.printStackTrace();
            msg.setText("Отсутсвует подключение с сервером");
        }
    }

    @FXML
    private Label msg;

    @FXML
    private TextField loginTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private void login() throws IOException {
        if(loginTF.getText().isEmpty()){
            msg.setText("Все поля должны быть заполнены");
            return;
        }
        if(passwordTF.getText().isEmpty()){
            msg.setText("Все поля должны быть заполнены");
            return;
        }
        Account a = new Account();
        a.setAccountName(loginTF.getText());
        a.setVerified(true);
        try{
            a.send();
            a = Account.read();
        } catch (IOException e){
            e.printStackTrace();
            msg.setText("Отсутсвует подключение с сервером");
        }  
        if(a.isVerified()){
            try
            {
                String ourPass = passwordTF.getText();
                String recievedEncryptedPass = a.getHash();
                String salt = a.getSalt();
                
                byte[] decodedKey = Base64.getDecoder().decode(salt);
                // rebuild key using SecretKeySpec
                SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, originalKey);
                String ourEncryptedPass = Base64.getEncoder().encodeToString(cipher.doFinal(ourPass.getBytes("UTF-8")));
                SecondaryController.setAcc(a);
                System.out.println(ourEncryptedPass+" == "+recievedEncryptedPass);
                if(ourEncryptedPass.equals(recievedEncryptedPass)){
                    a.send();
                    Stage stage = (Stage) passwordTF.getScene().getWindow();
                    Timeline timeline = new Timeline();
                    KeyFrame key = new KeyFrame(Duration.millis(1000),
                                new KeyValue (stage.getScene().getRoot().opacityProperty(), 0));
                    timeline.getKeyFrames().add(key);
                    timeline.setOnFinished(event->{
                        try {
                            App.setRoot("secondary");
                        } catch (Exception e) {}
                    });
                    timeline.play();

                } else {
                    a.setVerified(false);
                    a.send();
                    msg.setText("Неверный пароль или имя аккаунта");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            msg.setText(a.getAccountName());
        }
    }

    @FXML
    private void manageCreateAcc() throws IOException {
        App.setRoot("createAccount");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
