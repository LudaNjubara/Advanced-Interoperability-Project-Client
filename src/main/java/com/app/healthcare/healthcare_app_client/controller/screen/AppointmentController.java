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
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.app.healthcare.healthcare_app_client.utils.Fetchers.*;
import static com.app.healthcare.healthcare_app_client.utils.Utils.*;

public class AppointmentController implements Initializable {

    private Provider[] providers;
    private Patient[] patients;

    // TABLES
    @FXML
    private TableView<Appointment> currentAppointmentsTable;
    @FXML
    private TableView<Patient> currentPatientsTable;
    @FXML
    private TableView<Provider> currentProvidersTable;


    // APPOINTMENT TABLE COLUMNS
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> facilityColumn;
    @FXML
    private TableColumn<Appointment, String> providerColumn;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;

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


    // TEXT FIELDS
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private ComboBox<String> providerComboBox;
    @FXML
    private ComboBox<String> patientComboBox;

    // BUTTONS
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button editAppointmentButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMainTable();
        setupProvidersTable();
        setupPatientsTable();
        setupProvidersComboBox();
        setupPatientsComboBox();

        ContextMenu contextMenu = setupTableViewContextMenu();
        contextMenu.getItems().get(0).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(0)));
        contextMenu.getItems().get(1).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(1)));
        currentAppointmentsTable.setContextMenu(contextMenu);

        // create method that will handle clicking on a row in the table
        currentAppointmentsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> handleAppointmentSelection(newSelection)
        );

        addAppointmentButton.setVisible(true);
        editAppointmentButton.setVisible(false);
        patientComboBox.setDisable(true);
    }

    private void handleAppointmentSelection(Appointment newSelection) {
        if (newSelection != null) {
            Provider provider = newSelection.getProvider();
            Patient patient = newSelection.getPatient();

            currentProvidersTable.getItems().clear();
            currentProvidersTable.getItems().add(provider);

            currentPatientsTable.getItems().clear();
            currentPatientsTable.getItems().add(patient);
        }
    }


    // SETUP METHODS
    private void setupMainTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        facilityColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Facility facility = appointment.getFacility();
                if (facility != null) {
                    return new SimpleStringProperty(facility.getName());
                }
            }
            return new SimpleStringProperty("");
        });
        providerColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Provider provider = appointment.getProvider();
                if (provider != null) {
                    return new SimpleStringProperty(provider.getFirstName() + " " + provider.getLastName());
                }
            }
            return new SimpleStringProperty("");
        });
        patientColumn.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment != null) {
                Patient patient = appointment.getPatient();
                if (patient != null) {
                    return new SimpleStringProperty(patient.getFirstName() + " " + patient.getLastName());
                }
            }
            return new SimpleStringProperty("");
        });
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        currentAppointmentsTable.getItems().clear();
        currentAppointmentsTable.getItems().addAll(fetchAllAppointments());
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

    private void setupProvidersComboBox() {
        providers = fetchAllProviders();
        for (Provider provider : providers) {
            providerComboBox.getItems().add(provider.getFirstName() + " " + provider.getLastName() + " - " + provider.getFacility().getName());
        }
    }

    private void setupPatientsComboBox() {
        patients = fetchAllPatients();
        for (Patient patient : patients) {
            patientComboBox.getItems().add(patient.getFirstName() + " " + patient.getLastName() + " - " + patient.getFacility().getName());
        }
    }


    // CONTEXT MENU EVENT HANDLERS
    private void handleContextItemClicked(MenuItem menuItem) {
        if (menuItem.getText().equals("Delete")) {
            Appointment appointment = currentAppointmentsTable.getSelectionModel().getSelectedItem();
            handleDeleteAppointmentContextItemClick(appointment);
        } else if (menuItem.getText().equals("Edit")) {
            Appointment appointment = currentAppointmentsTable.getSelectionModel().getSelectedItem();
            handleEditAppointmentContextItemClick(appointment);
        }
    }

    private void handleDeleteAppointmentContextItemClick(Appointment appointment) {
        String APPOINTMENTS_URL = "http://localhost:8081/api/appointments/";

        RestTemplate restTemplate = setupRestTemplate();

        try {
            restTemplate.delete(APPOINTMENTS_URL + appointment.getId());
            setupMainTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditAppointmentContextItemClick(Appointment appointment) {
        addAppointmentButton.setVisible(false);
        editAppointmentButton.setVisible(true);

        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        dateTextField.setText(appointment.getAppointmentDate().toString());
        providerComboBox.setValue(appointment.getProvider().getFirstName() + " " + appointment.getProvider().getLastName() + " - " + appointment.getProvider().getFacility().getName());
        patientComboBox.setValue(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() + " - " + appointment.getPatient().getFacility().getName());
    }


    // EVENT HANDLERS
    @FXML
    public void handleProviderComboBoxChange() {
        String providerComboBoxValue = providerComboBox.getValue();
        String providerFirstName = providerComboBoxValue.split(" ")[0];
        String providerLastName = providerComboBoxValue.split(" ")[1];
        String providerFacilityName = providerComboBoxValue.substring(providerComboBoxValue.lastIndexOf("-") + 2);
        Long providerId = null;
        for (Provider provider_iterable : providers) {
            if (provider_iterable.getFirstName().equals(providerFirstName) && provider_iterable.getLastName().equals(providerLastName) && provider_iterable.getFacility().getName().equals(providerFacilityName)) {
                providerId = provider_iterable.getId();
                break;
            }
        }
        assert providerId != null;
        patientComboBox.setDisable(false);
        patients = fetchAllPatientsByProviderId(providerId);
        patientComboBox.getItems().clear();
        for (Patient patient_iterable : patients) {
            patientComboBox.getItems().add(patient_iterable.getFirstName() + " " + patient_iterable.getLastName() + " - " + patient_iterable.getFacility().getName());
        }
    }

    @FXML
    public void handleEditAppointmentButtonClick() {
        Long id = currentAppointmentsTable.getSelectionModel().getSelectedItem().getId();
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        LocalDate date = convertStringToLocalDate(dateTextField.getText());
        // Get provider, patient and facility from combo box values
        String providerComboBoxValue = providerComboBox.getValue();
        String patientComboBoxValue = patientComboBox.getValue();
        String providerFirstName = providerComboBoxValue.split(" ")[0];
        String providerLastName = providerComboBoxValue.split(" ")[1];
        String providerFacilityName = providerComboBoxValue.substring(providerComboBoxValue.lastIndexOf("-") + 2);
        String patientFirstName = patientComboBoxValue.split(" ")[0];
        String patientLastName = patientComboBoxValue.split(" ")[1];
        String patientFacilityName = patientComboBoxValue.substring(patientComboBoxValue.lastIndexOf("-") + 2);
        Provider provider = null;
        Patient patient = null;
        Facility facility = null;
        for (Provider provider_iterable : providers) {
            if (provider_iterable.getFirstName().equals(providerFirstName) && provider_iterable.getLastName().equals(providerLastName) && provider_iterable.getFacility().getName().equals(providerFacilityName)) {
                provider = provider_iterable;
                facility = provider_iterable.getFacility();
                break;
            }
        }

        for (Patient patient_iterable : patients) {
            if (patient_iterable.getFirstName().equals(patientFirstName) && patient_iterable.getLastName().equals(patientLastName) && patient_iterable.getFacility().getName().equals(patientFacilityName)) {
                patient = patient_iterable;
                break;
            }
        }

        RestTemplate restTemplate = setupRestTemplate();

        assert provider != null;
        assert patient != null;
        assert facility != null;
        System.out.println("Provider: " + provider);
        System.out.println("Patient: " + patient);
        System.out.println("Facility: " + facility);
        Appointment appointment = new Appointment(title, description, date, provider, patient, facility);

        System.out.println("Appointment: " + appointment);

        try {
            String BASE_URL = "http://localhost:8081/api/appointments/";
            ResponseEntity<Appointment> responseEntity = restTemplate.exchange(BASE_URL + id, HttpMethod.PUT, new HttpEntity<>(appointment), Appointment.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Appointment updated successfully.", editAppointmentButton);
                clearFields();
                addAppointmentButton.setVisible(true);
                editAppointmentButton.setVisible(false);
                patientComboBox.setDisable(true);
                setupMainTable();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addAppointmentButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddAppointmentButtonClick() {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        LocalDate date = convertStringToLocalDate(dateTextField.getText());

        // Get provider, patient and facility ids from combo box values
        String providerComboBoxValue = providerComboBox.getValue();
        String providerFirstName = providerComboBoxValue.split(" ")[0];
        String providerLastName = providerComboBoxValue.split(" ")[1];
        String patientFirstName = patientComboBox.getValue().split(" ")[0];
        String patientLastName = patientComboBox.getValue().split(" ")[1];
        String providerFacilityName = providerComboBoxValue.substring(providerComboBoxValue.lastIndexOf("-") + 2);
        Long providerId = null;
        Long patientId = null;
        Long facilityId = null;

        for (Provider provider : providers) {
            if (provider.getFirstName().equals(providerFirstName) && provider.getLastName().equals(providerLastName) && provider.getFacility().getName().equals(providerFacilityName)) {
                providerId = provider.getId();
                facilityId = provider.getFacility().getId();
                break;
            }
        }

        for (Patient patient : patients) {
            if (patient.getFirstName().equals(patientFirstName) && patient.getLastName().equals(patientLastName)) {
                patientId = patient.getId();
                break;
            }
        }


        RestTemplate restTemplate = setupRestTemplate();

        assert providerId != null;
        assert patientId != null;
        assert facilityId != null;
        System.out.println("ProviderId: " + providerId);
        System.out.println("PatientId: " + patientId);
        System.out.println("FacilityId: " + facilityId);
        Appointment appointment = new Appointment(title, description, date, providerId, patientId, facilityId);

        System.out.println("Appointment: " + appointment);

        try {
            String BASE_URL = "http://localhost:8081/api/appointments";
            ResponseEntity<Appointment> responseEntity = restTemplate.postForEntity(BASE_URL, appointment, Appointment.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Utils.showNotification("Success", "Appointment added successfully.", addAppointmentButton);
                clearFields();
                setupMainTable();
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
                Utils.showNotification("Error", "Request failed with status code: " + responseEntity.getStatusCode(), addAppointmentButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // CLEANUP
    private void clearFields() {
        titleTextField.clear();
        descriptionTextField.clear();
        dateTextField.clear();
        providerComboBox.setValue(null);
        patientComboBox.setValue(null);
    }
}
