package com.app.healthcare.healthcare_app_client.controller.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane contentWrapper;

    @FXML
    private SidebarController sidebarController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set a reference to the MainController in the SidebarController
        sidebarController.setMainController(this);

    }

    public void setScreen(AnchorPane screen) {
        try {
            contentWrapper.getChildren().setAll(screen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSidebarButtonClick(FXMLLoader loader) throws IOException {
        // Load the screen FXML
        AnchorPane screen = loader.load();

        // Set the screen in the content wrapper
        setScreen(screen);
    }
}

