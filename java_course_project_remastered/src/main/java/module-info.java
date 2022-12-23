module java_course_project_remastered {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens java_course_project_remastered to javafx.fxml;
    opens Models to com.google.gson;
    exports java_course_project_remastered;
    exports Models;
}
