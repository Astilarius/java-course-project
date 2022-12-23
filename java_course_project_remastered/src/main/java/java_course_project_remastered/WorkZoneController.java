package java_course_project_remastered;

import java.io.IOException;

import Models.Expense;
import Models.Material;
import Models.ProdResult;
import Models.Production;
import Models.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class WorkZoneController {
    private static Project project;

    @FXML
    /*static*/FlowPane workZoneFP;

    @FXML
    Label projectInfo;

    @FXML
    Label msg;

    public void initialize(){
        projectInfo.setText("имя проекта - "+project.getName()+"; автор - "+project.getAuthor().getAccountName()+"; стоимость товара - "+project.getPrice()+";");
        if(!project.getExpenses().isEmpty()){
            Expense e = project.getExpenses().iterator().next();
            e.write(workZoneFP);
            
            ProdResult finalRes = new ProdResult();
            finalRes.linked.add(e);
            workZoneFP.getChildren().add(finalRes.getProdRes());
        } else {
            ProdResult finalRes = new ProdResult();
            workZoneFP.getChildren().clear();
            workZoneFP.getChildren().add(finalRes.getProdRes());
        }
        
    }

    public static void addExpence(Expense e){
        project.expenses.add(e);
        // workZoneFP.getChildren().addAll(e.getElem());
    }

    public static void setProject(Project p){
        System.out.println("project set!");
        WorkZoneController.project = p;
    }

    
    @FXML
    private void save() throws IOException {
        if(ProdResult.linked.isEmpty()){
            msg.setText("К блоку 'Результат' ни привязан ни один другой блок, поэтому программой проект будет считаться пустым");
            return;
        } else {
            msg.setText("");
            project.setExpenses(ProdResult.linked);
            project.send();
        }
    }
    
    @FXML
    private void exitSave() throws IOException {
        if(ProdResult.linked.isEmpty() && !project.getExpenses().isEmpty()){
            msg.setText("К блоку 'Результат' ни привязан ни один другой блок, поэтому программой проект будет считаться пустым");
            return;
        }
        project.setExpenses(ProdResult.linked);
        try{
            project.send();
        }
        catch (IOException e){
            e.printStackTrace();
            msg.setText("Отсутсвует подключение с сервером");
        }
        App.setRoot("secondary");
    }
    
    @FXML
    private void addMat() throws IOException {
        Stage thisStage = (Stage) workZoneFP.getScene().getWindow();
        Material.CreateElem(workZoneFP, thisStage);
        // App.setRoot("createMat");
    }
    
    @FXML
    private void addProd() throws IOException {
        Stage thisStage = (Stage) workZoneFP.getScene().getWindow();
        Production.CreateElem(workZoneFP, thisStage);
        // App.setRoot("createMat");
    }
}
