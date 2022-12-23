module java_course_project_server {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;

    opens java_course_project_server to javafx.fxml;
    opens Models to com.google.gson;
    exports java_course_project_server;
    exports Models;
}