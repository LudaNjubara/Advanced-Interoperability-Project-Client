module com.app.healthcare.healthcare_app_client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires spring.web;


    opens com.app.healthcare.healthcare_app_client to javafx.fxml;
    exports com.app.healthcare.healthcare_app_client;
    exports com.app.healthcare.healthcare_app_client.controller.view;
    opens com.app.healthcare.healthcare_app_client.controller.view to javafx.fxml;
    exports com.app.healthcare.healthcare_app_client.controller.screen;
    opens com.app.healthcare.healthcare_app_client.controller.screen to javafx.fxml;
}