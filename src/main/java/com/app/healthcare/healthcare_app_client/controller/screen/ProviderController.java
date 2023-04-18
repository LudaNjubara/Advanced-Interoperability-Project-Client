package com.app.healthcare.healthcare_app_client.controller.screen;

import com.app.healthcare.healthcare_app_client.model.Facility;
import com.app.healthcare.healthcare_app_client.model.Provider;
import com.app.healthcare.healthcare_app_client.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;

import static com.app.healthcare.healthcare_app_client.utils.Utils.setupRestTemplate;

public class ProviderController implements Initializable {

    private Facility[] facilities;

    @FXML
    private TableView<Provider> currentProvidersTable;

    @FXML
    private TableColumn<Provider, String> firstNameColumn;
    @FXML
    private TableColumn<Provider, String> lastNameColumn;
    @FXML
    private TableColumn<Provider, String> addressColumn;
    @FXML
    private TableColumn<Provider, String> phoneNumberColumn;
    @FXML
    private TableColumn<Provider, String> emailColumn;
    @FXML
    private TableColumn<Provider, String> oibColumn;
    @FXML
    private TableColumn<Provider, String> imageURLColumn;


    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField oibTextField;
    @FXML
    private TextField imageURLTextField;
    @FXML
    private ComboBox<String> facilityComboBox;

    @FXML
    private Button addProviderButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupFacilitiesComboBox();
    }

    private void setupTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        oibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentProvidersTable.getItems().clear();
        currentProvidersTable.getItems().addAll(fetchAllProviders());
    }

    private void setupFacilitiesComboBox() {
        facilities = fetchAllFacilities();
        for (Facility facility : facilities) {
            facilityComboBox.getItems().add(facility.getName());
        }
    }

    private Provider[] fetchAllProviders() {
        Provider[] returnedProviders = null;
        String URL = "http://localhost:8081/api/providers";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Provider[]> responseEntity = restTemplate.getForEntity(URL, Provider[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedProviders = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedProviders;
    }

    private Facility[] fetchAllFacilities() {
        Facility[] returnedFacilities = null;
        String URL = "http://localhost:8081/api/facilities";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            ResponseEntity<Facility[]> responseEntity = restTemplate.getForEntity(URL, Facility[].class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                returnedFacilities = responseEntity.getBody();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedFacilities;
    }

    @FXML
    public void handleAddProviderButtonClick() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String oib = oibTextField.getText();
        String imageURL = imageURLTextField.getText();
        // Get facility ID from facility name
        String facilityName = facilityComboBox.getValue();
        Long facilityId = null;
        for (Facility facility : facilities) {
            if (facility.getName().equals(facilityName)) {
                facilityId = facility.getId();
                break;
            }
        }

        RestTemplate restTemplate = setupRestTemplate();

        Provider provider = new Provider(facilityId, firstName, lastName, address, phoneNumber, email, oib, imageURL);

        try {
            String BASE_URL = "http://localhost:8081/api/providers";
            ResponseEntity<Provider> responseEntity = restTemplate.postForEntity(BASE_URL, provider, Provider.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Provider created successfully.", addProviderButton);
                clearFields();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addProviderButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        addressTextField.clear();
        phoneNumberTextField.clear();
        emailTextField.clear();
        oibTextField.clear();
        imageURLTextField.clear();
        facilityComboBox.getSelectionModel().clearSelection();
    }
}
