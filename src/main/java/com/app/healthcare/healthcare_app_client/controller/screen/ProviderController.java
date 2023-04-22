package com.app.healthcare.healthcare_app_client.controller.screen;

import com.app.healthcare.healthcare_app_client.model.Facility;
import com.app.healthcare.healthcare_app_client.model.Provider;
import com.app.healthcare.healthcare_app_client.utils.Utils;
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

import static com.app.healthcare.healthcare_app_client.utils.Fetchers.fetchAllFacilities;
import static com.app.healthcare.healthcare_app_client.utils.Fetchers.fetchAllProviders;
import static com.app.healthcare.healthcare_app_client.utils.Utils.setupRestTemplate;
import static com.app.healthcare.healthcare_app_client.utils.Utils.setupTableViewContextMenu;

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
    @FXML
    private Button editProviderButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        setupFacilitiesComboBox();
        ContextMenu contextMenu = setupTableViewContextMenu();

        contextMenu.getItems().get(0).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(0)));
        contextMenu.getItems().get(1).setOnAction(actionEvent -> handleContextItemClicked(contextMenu.getItems().get(1)));

        currentProvidersTable.setContextMenu(contextMenu);

        addProviderButton.setVisible(true);
        editProviderButton.setVisible(false);
    }

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
            setupTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditProviderContextItemClick(Provider provider) {
        String FACILITIES_URL = "http://localhost:8081/api/facilities/";
        RestTemplate rest = setupRestTemplate();

        firstNameTextField.setText(provider.getFirstName());
        lastNameTextField.setText(provider.getLastName());
        addressTextField.setText(provider.getAddress());
        phoneNumberTextField.setText(provider.getPhoneNumber());
        emailTextField.setText(provider.getEmail());
        oibTextField.setText(provider.getOib());
        imageURLTextField.setText(provider.getImageUrl());
        Facility facility;

        try {
            ResponseEntity<Facility> responseEntity = rest.getForEntity(FACILITIES_URL + provider.getFacility().getId(), Facility.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                facility = responseEntity.getBody();
                assert facility != null;
                facilityComboBox.setValue(facility.getName());
            } else {
                System.out.println("Request failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        addProviderButton.setVisible(false);
        editProviderButton.setVisible(true);
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
            } else {
                throw new RuntimeException("Facility not found with name: " + facilityName);
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
                setupTable();
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
