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

public class ProviderController implements Initializable {

    private Facility[] facilities;

    // TABLES
    @FXML
    private TableView<Provider> currentProvidersTable;
    @FXML
    private TableView<Patient> currentPatientsTable;
    @FXML
    private TableView<Appointment> currentAppointmentsTable;

    // PROVIDER TABLE COLUMNS
    @FXML
    private TableColumn<Provider, String> firstNameColumn;
    @FXML
    private TableColumn<Provider, String> lastNameColumn;
    @FXML
    private TableColumn<Provider, String> facilityColumn;
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
    private TextField emailTextField;
    @FXML
    private TextField oibTextField;
    @FXML
    private TextField imageURLTextField;
    @FXML
    private ComboBox<String> facilityComboBox;

    // BUTTONS
    @FXML
    private Button addProviderButton;
    @FXML
    private Button editProviderButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMainTable();
        setupPatientsTable();
        setupAppointmentsTable();
        setupFacilitiesComboBox();

        ContextMenu contextMenu = setupTableViewContextMenu();
        contextMenu.getItems().get(0).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(0)));
        contextMenu.getItems().get(1).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(1)));
        currentProvidersTable.setContextMenu(contextMenu);

        // create method that will handle clicking on a row in the table
        currentProvidersTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleProviderSelection(newSelection)
        );

        addProviderButton.setVisible(true);
        editProviderButton.setVisible(false);
    }

    private void handleProviderSelection(Provider newSelection) {
        if (newSelection != null) {
            Patient[] patients = fetchAllPatientsByProviderId(newSelection.getId());
            Appointment[] appointments = fetchAllAppointmentsByProviderId(newSelection.getId());

            currentPatientsTable.getItems().clear();
            currentPatientsTable.getItems().addAll(patients);

            currentAppointmentsTable.getItems().clear();
            currentAppointmentsTable.getItems().addAll(appointments);
        }
    }


    // SETUP METHODS
    private void setupMainTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        facilityColumn.setCellValueFactory(cellData -> {
            Provider provider = cellData.getValue();
            if (provider != null) {
                Facility facility = provider.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        oibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentProvidersTable.getItems().clear();
        currentProvidersTable.getItems().addAll(fetchAllProviders());
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
        patientImageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));

        currentPatientsTable.getItems().clear();
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

    private void setupFacilitiesComboBox() {
        facilities = fetchAllFacilities();
        for (Facility facility : facilities) {
            facilityComboBox.getItems().add(facility.getName());
        }
    }


    // CONTEXT MENU EVENT HANDLERS
    private void handleContextItemClicked(MenuItem menuItem) {
        if (menuItem.getText().equals("Delete")) {
            Provider provider = currentProvidersTable.getSelectionModel().getSelectedItem();
            handleDeleteProviderContextItemClick(provider);
        } else if (menuItem.getText().equals("Edit")) {
            Provider provider = currentProvidersTable.getSelectionModel().getSelectedItem();
            handleEditProviderContextItemClick(provider);
        }
    }

    private void handleDeleteProviderContextItemClick(Provider provider) {
        String PROVIDERS_URL = "http://localhost:8081/api/providers/";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            restTemplate.delete(PROVIDERS_URL + provider.getId());
            setupMainTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditProviderContextItemClick(Provider provider) {
        firstNameTextField.setText(provider.getFirstName());
        lastNameTextField.setText(provider.getLastName());
        facilityComboBox.setValue(provider.getFacility().getName());
        addressTextField.setText(provider.getAddress());
        phoneNumberTextField.setText(provider.getPhoneNumber());
        emailTextField.setText(provider.getEmail());
        oibTextField.setText(provider.getOib());
        imageURLTextField.setText(provider.getImageUrl());

        addProviderButton.setVisible(false);
        editProviderButton.setVisible(true);
    }


    // BUTTON EVENT HANDLERS
    @FXML
    public void handleEditProviderButtonClick() {
        Long id = currentProvidersTable.getSelectionModel().getSelectedItem().getId();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String oib = oibTextField.getText();
        String imageURL = imageURLTextField.getText();
        // Get facility ID from facility name
        String facilityName = facilityComboBox.getValue();
        Facility facility = null;
        for (Facility facility_iterable : facilities) {
            if (facility_iterable.getName().equals(facilityName)) {
                facility = facility_iterable;
                break;
            }
        }

        RestTemplate restTemplate = setupRestTemplate();

        assert facility != null;
        System.out.println("Provider ID:" + id);
        System.out.println("Facility: " + facility);
        Provider provider = new Provider(facility, firstName, lastName, address, phoneNumber, email, oib, imageURL);

        System.out.println("Provider: " + provider);

        try {
            String BASE_URL = "http://localhost:8081/api/providers/";
            ResponseEntity<Provider> responseEntity = restTemplate.exchange(BASE_URL + id, HttpMethod.PUT, new HttpEntity<>(provider), Provider.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Provider updated successfully.", editProviderButton);
                clearFields();
                setupMainTable();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addProviderButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        for (Facility facility_iterable : facilities) {
            if (facility_iterable.getName().equals(facilityName)) {
                facilityId = facility_iterable.getId();
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


    // CLEANUP
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
