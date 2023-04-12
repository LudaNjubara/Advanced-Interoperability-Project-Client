package com.app.healthcare.healthcare_app_client.controller.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class SidebarController {
    // Reference to the MainController
    private MainController mainController;

    // Method called when sidebar button is clicked
    public void handleButtonClicked(ActionEvent event) throws IOException {
         String buttonId = ((Button) event.getSource()).getId();
        FXMLLoader loader;

        switch (buttonId) {
            case "homeSidebarButton" -> loader = new FXMLLoader(getClass()
                    .getResource("/com/app/healthcare/healthcare_app_client/screens/home.fxml"));
            case "patientSidebarButton" -> loader = new FXMLLoader(getClass()
                    .getResource("/com/app/healthcare/healthcare_app_client/screens/patients.fxml"));
            case "providerSidebarButton" -> loader = new FXMLLoader(getClass()
                    .getResource("/com/app/healthcare/healthcare_app_client/screens/providers.fxml"));
            case "appointmentSidebarButton" -> loader = new FXMLLoader(getClass()
                    .getResource("/com/app/healthcare/healthcare_app_client/screens/appointments.fxml"));
            case "facilitySidebarButton" -> loader = new FXMLLoader(getClass()
                    .getResource("/com/app/healthcare/healthcare_app_client/screens/facilities.fxml"));
            default -> throw new IllegalStateException("Unexpected value: " + buttonId);
        }
        // Call the handleSidebarButtonClicked method of the MainController
        mainController.handleSidebarButtonClick(loader);
    }

    // Setter method for the MainController reference
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
