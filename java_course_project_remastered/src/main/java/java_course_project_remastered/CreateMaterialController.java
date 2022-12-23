package java_course_project_remastered;

import Models.Material;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreateMaterialController {
    @FXML
    private TextField nameTF;
    @FXML
    private TextField priceTF;
    @FXML
    private TextField amountTF;
    @FXML
    private Label msg;
    @FXML
    private void submit(){
        String text = nameTF.getText();
        String text2 = priceTF.getText();
        String text3 = amountTF.getText();
        if(!text2.matches("^[\\d]+(\\.[\\d]+)?$")){
            msg.setText("Некорректный ввод");
        } else if(!text3.matches("^[\\d]+$")){
            msg.setText("Некорректный ввод");
        } else {
            Material prod = new Material(text, Double.parseDouble(text2), Integer.parseInt(text3));
            // materials.add(prod);
            VBox elem = prod.getElem();
            WorkZoneController.addExpence(prod);
        }
        // WorkZoneController.addExpence(null);
    }
    public void initialize(){
        
    }
}
