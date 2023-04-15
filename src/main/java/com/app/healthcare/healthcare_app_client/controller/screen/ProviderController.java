package com.app.healthcare.healthcare_app_client.controller.screen;

import com.app.healthcare.healthcare_app_client.model.Provider;
import com.app.healthcare.healthcare_app_client.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProviderController implements Initializable {

    private final String BASE_URL = "http://localhost:8081/api/providers";

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
    private Button addProviderButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        oibColumn.setCellValueFactory(new PropertyValueFactory<>("oib"));
        imageURLColumn.setCellValueFactory(new PropertyValueFactory<>("imageURL"));
    }

    @FXML
    public void handleAddProviderFormSubmit() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String oib = oibTextField.getText();
        String imageURL = imageURLTextField.getText();
        Long facilityId = 2L;

        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypes);
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        Provider provider = new Provider(facilityId, firstName, lastName, address, phoneNumber, email, oib, imageURL);

        try {
            ResponseEntity<Provider> responseEntity = restTemplate.postForEntity(BASE_URL, provider, Provider.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Provider providerResponse = responseEntity.getBody();

                System.out.println(providerResponse);
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
    }
}
