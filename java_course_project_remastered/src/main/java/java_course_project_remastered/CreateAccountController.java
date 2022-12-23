package java_course_project_remastered;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Models.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateAccountController {
    @FXML
    Label msg;
    @FXML
    TextField loginTF;
    @FXML
    TextField passwordTF;
    @FXML
    TextField passwordCopyTF;
    @FXML
    public void createAcc() throws IOException{
        if(loginTF.getText().isEmpty()){
            msg.setText("Все поля должны быть заполнены");
            return;
        }
        if(passwordTF.getText().isEmpty()){
            msg.setText("Все поля должны быть заполнены");
            return;
        }
        if(passwordCopyTF.getText().isEmpty()){
            msg.setText("Все поля должны быть заполнены");
            return;
        }
        if(passwordTF.getText().equals(passwordCopyTF.getText())){
            Account a = new Account();
            a.setAccountName(loginTF.getText());
            a.setVerified(false);
            try{
                // create new key
                SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                // get base64 encoded version of the key
                String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                String encodedPassword = Base64.getEncoder().encodeToString(cipher.doFinal(passwordTF.getText().getBytes("UTF-8")));
                a.setHash(encodedPassword);
                a.setSalt(encodedKey);
                a.send();
            } catch (IOException e){
                e.printStackTrace();
                msg.setText("Отсутсвует подключение с сервером");
            } catch (Exception e){
                e.printStackTrace();
            }
            Account rec = Account.read();
            if(a.equals(rec)){
                App.setRoot("primary");
            } else { 
                msg.setText(rec.getAccountName());
            }
        } else {
            msg.setText("Пароли не совпадают");
        }
    }
    @FXML
    public void switchToPrimary() throws IOException{
        App.setRoot("primary");
    }
}
