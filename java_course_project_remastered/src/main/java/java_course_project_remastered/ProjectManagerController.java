package java_course_project_remastered;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Models.Account;
import Models.Expense;
import Models.ExpenseDeserializer;
import Models.Project;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ProjectManagerController {
    private static Account acc;
    public static void setAcc(Account a){
        acc = a;
    }
    public void initialize() throws IOException{
        myProjectBox.getParent().setVisible(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), myProjectBox.getParent());
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        myProjectBox.getParent().setVisible(true);
        ft.play();
        Project p = new Project();
        
        p.setId(-1);
        p.setAuthor(acc);
        try{
            p.send(); 
        } catch (IOException e){
            e.printStackTrace();
            msg.setText("Отсутсвует подключение с сервером");
        }  
        p.setId(0);
        while(p.getId() != -1){
            p = Project.read();
            if(p.getId() == -1) continue;
            if(p.getAuthor().getAccountName().equals(acc.getAccountName())){
                HBox hb = new HBox();
                Label l = new Label("id = "+p.getId()+" имя проекта = "+p.getName()+" автор проекта = "+p.getAuthor().getAccountName()+" стоимость товара = "+p.getPrice());
                Button open = new Button("открыть");
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
                builder.setPrettyPrinting();
                Gson gson = builder.create();
                String jsonID = gson.toJson(p);
                open.setId(jsonID);
                open.setOnAction(event->{
                    String json = open.getId();
                    Project res = gson.fromJson(json, Project.class);
                    WorkZoneController.setProject(res);
                    try {
                        App.setRoot("workZone");                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Button delete = new Button("удалить");
                delete.setId(jsonID);
                delete.setOnAction(event->{
                    String json = open.getId();
                    Project res = gson.fromJson(json, Project.class);
                    res.setId(res.getId()*-1 - 1);
                    try {
                        res.send();                     
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg.setText("Отсутсвует подключение с сервером");
                    }
                });
                hb.getChildren().addAll(l, open, delete);
                myProjectBox.getChildren().addAll(hb);
            } else if (p.isPublic()) {
                HBox hb = new HBox();
                Label l = new Label("id = "+p.getId()+" имя проекта = "+p.getName()+" автор проекта = "+p.getAuthor().getAccountName()+" стоимость товара = "+p.getPrice());
                Button open = new Button("открыть");
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
                builder.setPrettyPrinting();
                Gson gson = builder.create();
                String jsonID = gson.toJson(p);
                open.setId(jsonID);
                open.setOnAction(event->{
                    String json = open.getId();
                    Project res = gson.fromJson(json, Project.class);
                    WorkZoneController.setProject(res);
                    try {
                        App.setRoot("workZone");                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                hb.getChildren().addAll(l, open);
                publicProjectBox.getChildren().addAll(hb);
            } else {
                HBox hb = new HBox();
                Label l = new Label("id = "+p.getId()+" имя проекта = "+p.getName()+" автор проекта = "+p.getAuthor().getAccountName()+" стоимость товара = "+p.getPrice());
                Button open = new Button("открыть");
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Expense.class, new ExpenseDeserializer());
                builder.setPrettyPrinting();
                Gson gson = builder.create();
                String jsonID = gson.toJson(p);
                open.setId(jsonID);
                open.setOnAction(event->{
                    String json = open.getId();
                    Project res = gson.fromJson(json, Project.class);
                    WorkZoneController.setProject(res);
                    try {
                        App.setRoot("workZone");                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                hb.getChildren().addAll(l, open);
                whitelistedProjectBox.getChildren().addAll(hb);
            }
        }   
    }
    @FXML
    private Label msg;
    
    @FXML
    private VBox myProjectBox;
    
    @FXML
    private VBox publicProjectBox;
    
    @FXML
    private VBox whitelistedProjectBox;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    public static class MyProject{
 
        private SimpleStringProperty firstName;
        private SimpleStringProperty lastName;
        private SimpleStringProperty email;

    }
}
