package java_course_project_remastered;

import java.io.IOException;

import Models.Account;
import Models.Project;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class CreateProjectController {
    static Account acc;
    public static void setAcc(Account a){
        acc = a;
    }
    public void initialize() throws IOException{
        msg.getParent().setVisible(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), msg.getParent());
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        msg.getParent().setVisible(true);
        ft.play();
    }

    @FXML
    Label msg;

    @FXML
    TextField nameTF;

    @FXML
    TextField whiteListTF;

    @FXML
    ListView<String> whiteListLV;

    @FXML
    CheckBox publicCB;

    @FXML
    public void addToWhiteList(KeyEvent event){
        KeyCode code = event.getCode();
        if (code.name() == "ENTER"){
            whiteListLV.getItems().addAll(whiteListTF.getText());
            whiteListTF.setText("");
        }
    }
    @FXML
    public void removeFromWhiteList(KeyEvent event){
        KeyCode code = event.getCode();
        if (code.name() == "DELETE"){
            String toRemove = whiteListLV.getSelectionModel().getSelectedItem();
            whiteListLV.itemsProperty().get().remove(toRemove);
        }
    }
    @FXML
    public void switchToSecondary() throws IOException{
        App.setRoot("secondary");
    }
    @FXML
    public void createProject() throws IOException{
        if (nameTF.getText().isEmpty()) {
            msg.setText("Название проекта не должно быть пустым");
            return;
        }
        Project p = new Project();
        p.setAuthor(acc);
        p.setName(nameTF.getText());
        
        System.out.println(publicCB.isSelected());
        if(publicCB.isSelected()){
            if (!whiteListLV.getItems().isEmpty()){
                msg.setText("Проект не может одновременно быть публичным и содержать список пользователей, которые могут его изменять и просматривать\n(т.к. любой пользователь может изменять и просмптривать публичные проекты)");
                return;
            }
            p.setPublic(true);
        } else  {
            p.setPublic(false);
        }
        for(String s : whiteListLV.getItems()){
            if (acc.getAccountName().equals(s)){
                msg.setText("Список пользователей, которые могут изменять и просматривать проект не должен содержать вас\n(т.к. вы являетесь его создателем и всегда можете его изменять и просматривать)");
                return;
            }
            p.whiteList.add(s);
        }
        try{
            p.send();
            p = Project.read();
        } catch (IOException e){
            e.printStackTrace();
            msg.setText("Отсутсвует подключение с сервером");
        }  
        WorkZoneController.setProject(p);
        App.setRoot("workZone");
    }
}
