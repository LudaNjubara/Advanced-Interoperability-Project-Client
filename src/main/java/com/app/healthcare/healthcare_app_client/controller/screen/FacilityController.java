package com.app.healthcare.healthcare_app_client.controller.screen;

import com.app.healthcare.healthcare_app_client.model.Facility;
import com.app.healthcare.healthcare_app_client.model.Patient;
import com.app.healthcare.healthcare_app_client.model.Provider;
import com.app.healthcare.healthcare_app_client.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ResourceBundle;

import static com.app.healthcare.healthcare_app_client.utils.Fetchers.*;
import static com.app.healthcare.healthcare_app_client.utils.Utils.setupRestTemplate;
import static com.app.healthcare.healthcare_app_client.utils.Utils.setupTableViewContextMenu;

public class FacilityController implements Initializable {

    // TABLES
    @FXML
    private TableView<Facility> currentFacilitiesTable;
    @FXML
    private TableView<Provider> currentProvidersTable;
    @FXML
    private TableView<Patient> currentPatientsTable;

    // FACILITY TABLE COLUMNS
    @FXML
    private TableColumn<Facility, String> nameColumn;
    @FXML
    private TableColumn<Facility, String> addressColumn;
    @FXML
    private TableColumn<Facility, String> emailColumn;
    @FXML
    private TableColumn<Facility, String> imageURLColumn;

    // PROVIDER TABLE COLUMNS
    @FXML
    private TableColumn<Provider, String> providerFirstNameColumn;
    @FXML
    private TableColumn<Provider, String> providerLastNameColumn;
    @FXML
    private TableColumn<Provider, String> providerFacilityColumn;
    @FXML
    private TableColumn<Provider, String> providerAddressColumn;
    @FXML
    private TableColumn<Provider, String> providerPhoneNumberColumn;
    @FXML
    private TableColumn<Provider, String> providerEmailColumn;
    @FXML
    private TableColumn<Provider, String> providerOibColumn;
    @FXML
    private TableColumn<Provider, String> providerImageURLColumn;

    // PATIENT TABLE COLUMNS
    @FXML
    private TableColumn<Patient, String> patientFirstNameColumn;
    @FXML
    private TableColumn<Patient, String> patientLastNameColumn;
    @FXML
    private TableColumn<Patient, String> patientFacilityColumn;
    @FXML
    private TableColumn<Patient, String> patientAddressColumn;
    @FXML
    private TableColumn<Patient, String> patientPhoneNumberColumn;
    @FXML
    private TableColumn<Patient, String> patientDOBColumn;
    @FXML
    private TableColumn<Patient, String> patientOibColumn;
    @FXML
    private TableColumn<Patient, String> patientImageURLColumn;

    // TEXT FIELDS
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField imageURLTextField;

    // BUTTONS
    @FXML
    private Button addFacilityButton;
    @FXML
    private Button editFacilityButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMainTable();
        setupPatientsTable();
        setupProvidersTable();

        ContextMenu contextMenu = setupTableViewContextMenu();
        contextMenu.getItems().get(0).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(0)));
        contextMenu.getItems().get(1).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(1)));
        currentFacilitiesTable.setContextMenu(contextMenu);

        // create method that will handle clicking on a row in the table
        currentFacilitiesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleFacilitySelection(newSelection)
        );

        addFacilityButton.setVisible(true);
        editFacilityButton.setVisible(false);
    }

    private void handleFacilitySelection(Facility newSelection) {
        if (newSelection != null) {
            Patient[] patients = fetchAllPatientsByFacilityId(newSelection.getId());
            Provider[] providers = fetchAllProvidersByFacilityId(newSelection.getId());

            currentPatientsTable.getItems().clear();
            currentPatientsTable.getItems().addAll(patients);

            currentProvidersTable.getItems().clear();
            currentProvidersTable.getItems().addAll(providers);
        }
    }

    // SETUP METHODS
    private void setupMainTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentFacilitiesTable.getItems().clear();
        currentFacilitiesTable.getItems().addAll(fetchAllFacilities());
    }

    private void setupPatientsTable() {
        patientFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        patientLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        patientFacilityColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            if (patient != null) {
                Facility facility = patient.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        patientPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        patientDOBColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        patientOibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        patientImageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        currentPatientsTable.getItems().clear();
    }

    private void setupProvidersTable() {
        providerFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        providerLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        providerFacilityColumn.setCellValueFactory(cellData -> {
            Provider provider = cellData.getValue();
            if (provider != null) {
                Facility facility = provider.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        providerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        providerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        providerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        providerOibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        providerImageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        currentProvidersTable.getItems().clear();
    }


    // CONTEXT MENU EVENT HANDLERS
    private void handleContextItemClicked(MenuItem menuItem) {
        if (menuItem.getText().equals("Delete")) {
            Facility facility = currentFacilitiesTable.getSelectionModel().getSelectedItem();
            handleDeleteFacilityContextItemClick(facility);
        } else if (menuItem.getText().equals("Edit")) {
            Facility facility = currentFacilitiesTable.getSelectionModel().getSelectedItem();
            handleEditFacilityContextItemClick(facility);
        }
    }

    private void handleDeleteFacilityContextItemClick(Facility facility) {
        String FACILITIES_URL = "http://localhost:8081/api/facilities/";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            restTemplate.delete(FACILITIES_URL + facility.getId());
            setupMainTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditFacilityContextItemClick(Facility facility) {
        nameTextField.setText(facility.getName());
        addressTextField.setText(facility.getAddress());
        emailTextField.setText(facility.getEmail());
        imageURLTextField.setText(facility.getImageUrl());

        addFacilityButton.setVisible(false);
        editFacilityButton.setVisible(true);
    }


    // BUTTON EVENT HANDLERS
    @FXML
    public void handleEditFacilityButtonClick() {
        Long id = currentFacilitiesTable.getSelectionModel().getSelectedItem().getId();
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String email = emailTextField.getText();
        String imageURL = imageURLTextField.getText();

        RestTemplate restTemplate = setupRestTemplate();

        Facility facility = new Facility(name, address, email, imageURL);

        try {
            String BASE_URL = "http://localhost:8081/api/facilities/";
            ResponseEntity<Facility> responseEntity = restTemplate.exchange(BASE_URL + id, HttpMethod.PUT, new HttpEntity<>(facility), Facility.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Facility updated successfully.", editFacilityButton);
                clearFields();
                setupMainTable();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), editFacilityButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddFacilityButtonClick() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String email = emailTextField.getText();
        String imageURL = imageURLTextField.getText();

        RestTemplate restTemplate = setupRestTemplate();

        Facility facility = new Facility(name, address, email, imageURL);

        try {
            String BASE_URL = "http://localhost:8081/api/facilities";
            ResponseEntity<Facility> responseEntity = restTemplate.postForEntity(BASE_URL, facility, Facility.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Facility created successfully.", addFacilityButton);
                clearFields();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addFacilityButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // CLEANUP
    private void clearFields() {
        nameTextField.clear();
        addressTextField.clear();
        emailTextField.clear();
        imageURLTextField.clear();
    }
}
