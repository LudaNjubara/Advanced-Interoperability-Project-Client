package com.app.healthcare.healthcare_app_client.controller.screen;

import com.app.healthcare.healthcare_app_client.model.Appointment;
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

public class PatientController implements Initializable {

    private Provider[] providers;

    // TABLES
    @FXML
    private TableView<Patient> currentPatientsTable;
    @FXML
    private TableView<Provider> currentProvidersTable;
    @FXML
    private TableView<Appointment> currentAppointmentsTable;


    // PATIENT TABLE COLUMNS
    @FXML
    private TableColumn<Patient, String> firstNameColumn;
    @FXML
    private TableColumn<Patient, String> lastNameColumn;
    @FXML
    private TableColumn<Patient, String> facilityColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, String> phoneNumberColumn;
    @FXML
    private TableColumn<Patient, String> dobColumn;
    @FXML
    private TableColumn<Patient, String> oibColumn;
    @FXML
    private TableColumn<Patient, String> imageURLColumn;

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

    // APPOINTMENT TABLE COLUMNS
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentFacilityColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentProviderColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentPatientColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;

    // TEXT FIELDS
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField oibTextField;
    @FXML
    private TextField imageURLTextField;
    @FXML
    private ComboBox<String> providerComboBox;

    // BUTTONS
    @FXML
    private Button addPatientButton;
    @FXML
    private Button editPatientButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMainTable();
        setupProvidersTable();
        setupAppointmentsTable();
        setupProvidersComboBox();

        ContextMenu contextMenu = setupTableViewContextMenu();
        contextMenu.getItems().get(0).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(0)));
        contextMenu.getItems().get(1).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(1)));
        currentPatientsTable.setContextMenu(contextMenu);

        // create method that will handle clicking on a row in the table
        currentPatientsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handlePatientSelection(newSelection)
        );

        addPatientButton.setVisible(true);
        editPatientButton.setVisible(false);
    }

    private void handlePatientSelection(Patient newSelection) {
        if (newSelection != null) {
            Provider provider = newSelection.getProvider();
            Appointment[] appointments = fetchAllAppointmentsByPatientId(newSelection.getId());

            currentProvidersTable.getItems().clear();
            currentProvidersTable.getItems().add(provider);

            currentAppointmentsTable.getItems().clear();
            currentAppointmentsTable.getItems().addAll(appointments);
        }
    }


    // SETUP METHODS
    private void setupMainTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        facilityColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            if (patient != null) {
                Facility facility = patient.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        oibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentPatientsTable.getItems().clear();
        currentPatientsTable.getItems().addAll(fetchAllPatients());
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
        providerImageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentProvidersTable.getItems().clear();
    }

    private void setupAppointmentsTable() {
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentFacilityColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Facility facility = appointment.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        appointmentProviderColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Provider provider = appointment.getProvider();
                if (provider != null) {
                    return new SimpleStringProperty(provider.getFirstName() + " " + provider.getLastName());
                }
            }
            return new SimpleStringProperty("");
        });
        appointmentPatientColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Patient patient = appointment.getPatient();
                if (patient != null) {
                    return new SimpleStringProperty(patient.getFirstName() + " " + patient.getLastName());
                }
            }
            return new SimpleStringProperty("");
        });
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        currentAppointmentsTable.getItems().clear();
    }

    private void setupProvidersComboBox() {
        providers = fetchAllProviders();
        for (Provider provider : providers) {
            providerComboBox.getItems().add(provider.getFirstName() + " " + provider.getLastName() + " - " + provider.getFacility().getName());
        }
    }


    // CONTEXT MENU EVENT HANDLERS
    private void handleContextItemClicked(MenuItem menuItem) {
        if (menuItem.getText().equals("Delete")) {
            Patient patient = currentPatientsTable.getSelectionModel().getSelectedItem();
            handleDeletePatientContextItemClick(patient);
        } else if (menuItem.getText().equals("Edit")) {
            Patient patient = currentPatientsTable.getSelectionModel().getSelectedItem();
            handleEditPatientContextItemClick(patient);
        }
    }

    private void handleDeletePatientContextItemClick(Patient patient) {
        String PROVIDERS_URL = "http://localhost:8081/api/patients/";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            restTemplate.delete(PROVIDERS_URL + patient.getId());
            setupMainTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditPatientContextItemClick(Patient patient) {
        firstNameTextField.setText(patient.getFirstName());
        lastNameTextField.setText(patient.getLastName());
        providerComboBox.setValue(patient.getProvider().getFirstName() + " " + patient.getProvider().getLastName() + " - " + patient.getProvider().getFacility().getName());
        addressTextField.setText(patient.getAddress());
        phoneNumberTextField.setText(patient.getPhoneNumber());
        dobTextField.setText(patient.getDateOfBirth());
        oibTextField.setText(patient.getOib());
        imageURLTextField.setText(patient.getImageUrl());

        addPatientButton.setVisible(false);
        editPatientButton.setVisible(true);
    }


    // BUTTON EVENT HANDLERS
    @FXML
    public void handleEditPatientButtonClick() {
        Long id = currentPatientsTable.getSelectionModel().getSelectedItem().getId();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String dob = dobTextField.getText();
        String oib = oibTextField.getText();
        String imageURL = imageURLTextField.getText();
        // Get provider and facility from providerComboBox value
        String providerComboBoxValue = providerComboBox.getValue();
        String providerFirstName = providerComboBoxValue.substring(0, providerComboBoxValue.indexOf(" "));
        String providerLastName = providerComboBoxValue.substring(providerComboBoxValue.indexOf(" ") + 1, providerComboBoxValue.lastIndexOf("-") - 1);
        String providerFacilityName = providerComboBoxValue.substring(providerComboBoxValue.lastIndexOf("-") + 2);
        Provider provider = null;
        Facility facility = null;
        for (Provider provider_iterable : providers) {
            if (provider_iterable.getFacility().getName().equals(providerFacilityName) && provider_iterable.getFirstName().equals(providerFirstName) && provider_iterable.getLastName().equals(providerLastName)) {
                provider = provider_iterable;
                facility = provider_iterable.getFacility();
                break;
            }
        }

        RestTemplate restTemplate = setupRestTemplate();

        assert provider != null;
        assert facility != null;
        System.out.println("Patient ID:" + id);
        System.out.println("Provider: " + provider);
        System.out.println("Facility: " + facility);
        Patient patient = new Patient(firstName, lastName, facility, provider, address, phoneNumber, dob, oib, imageURL);

        System.out.println("Patient: " + patient);

        try {
            String BASE_URL = "http://localhost:8081/api/patients/";
            ResponseEntity<Patient> responseEntity = restTemplate.exchange(BASE_URL + id, HttpMethod.PUT, new HttpEntity<>(patient), Patient.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Patient updated successfully.", editPatientButton);
                clearFields();
                setupMainTable();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addPatientButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPatientButtonClick() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String dob = dobTextField.getText();
        String oib = oibTextField.getText();
        String imageURL = imageURLTextField.getText();
        // Get provider and facility from providerComboBox value
        String providerComboBoxValue = providerComboBox.getValue();
        // example of providerComboBoxValue: "Ivan Ivanovic - Sveti Duh"
        String providerFirstName = providerComboBoxValue.substring(0, providerComboBoxValue.indexOf(" "));
        String providerLastName = providerComboBoxValue.substring(providerComboBoxValue.indexOf(" ") + 1, providerComboBoxValue.lastIndexOf(" -"));
        String providerFacilityName = providerComboBoxValue.substring(providerComboBoxValue.lastIndexOf("-") + 2);
        Long providerId = null;
        Long facilityId = null;
        for (Provider provider_iterable : providers) {
            if (provider_iterable.getFacility().getName().equals(providerFacilityName) && provider_iterable.getFirstName().equals(providerFirstName) && provider_iterable.getLastName().equals(providerLastName)) {
                providerId = provider_iterable.getId();
                facilityId = provider_iterable.getFacility().getId();
                break;
            }
        }

        RestTemplate restTemplate = setupRestTemplate();

        System.out.println("Facility name: " + providerFacilityName);
        System.out.println("Provider ID: " + providerId);
        System.out.println("Facility ID: " + facilityId);

        Patient patient = new Patient(firstName, lastName, facilityId, providerId, address, phoneNumber, dob, oib, imageURL);
        try {
            String BASE_URL = "http://localhost:8081/api/patients";
            ResponseEntity<Patient> responseEntity = restTemplate.postForEntity(BASE_URL, patient, Patient.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Patient created successfully.", addPatientButton);
                clearFields();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addPatientButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // CLEANUP
    private void clearFields() {
        firstNameTextField.clear();
        lastNameTextField.clear();
        addressTextField.clear();
        phoneNumberTextField.clear();
        dobTextField.clear();
        oibTextField.clear();
        imageURLTextField.clear();
        providerComboBox.getSelectionModel().clearSelection();
    }
}
