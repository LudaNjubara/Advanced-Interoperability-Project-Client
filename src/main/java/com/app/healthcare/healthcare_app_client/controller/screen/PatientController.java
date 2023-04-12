package com.app.healthcare.healthcare_app_client.controller.screen;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchAllPatients();
    }

    private void fetchAllPatients() {
        System.out.println("Fetching all patients");
    }
}
