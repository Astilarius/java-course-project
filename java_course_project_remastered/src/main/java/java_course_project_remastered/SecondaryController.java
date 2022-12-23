package java_course_project_remastered;

import java.io.IOException;

import Models.Account;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SecondaryController {
    private static Account acc;
    public void initialize() throws IOException{
        b.getParent().setVisible(false);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), b.getParent());
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        b.getParent().setVisible(true);
        ft.play();
    }
    public static void setAcc(Account a){
        acc = a;
    }

    @FXML
    Button b;
    @FXML
    Button b2;
    @FXML
    Button b3;

    @FXML
    private void switchToPrimary() throws IOException {
        Stage stage = (Stage) b.getScene().getWindow();
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(1000),
                    new KeyValue (stage.getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished(event->{
            try {
                App.setRoot("primary");
            } catch (Exception e) {}
        });
        timeline.play();
    }

    @FXML
    private void switchToProjectManager() throws IOException {
        Stage stage = (Stage) b.getScene().getWindow();
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(1000),
                    new KeyValue (stage.getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished(event->{
            try {
                ProjectManagerController.setAcc(acc);
                App.setRoot("projectManager");
            } catch (Exception e) {}
        });
        timeline.play();
    }

    @FXML
    private void switchToCreateProject() throws IOException {
        Stage stage = (Stage) b.getScene().getWindow();
        Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(1000),
                    new KeyValue (stage.getScene().getRoot().opacityProperty(), 0));
        timeline.getKeyFrames().add(key);
        timeline.setOnFinished(event->{
            try {
                CreateProjectController.setAcc(acc);
                App.setRoot("createProject");
            } catch (Exception e) {}
        });
        timeline.play();
    }
}