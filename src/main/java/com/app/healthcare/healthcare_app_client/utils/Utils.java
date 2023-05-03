package com.app.healthcare.healthcare_app_client.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void showNotification(String title, String message, Node parent) {
        Notifications notifications = Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(5))
                .owner(parent.getScene().getWindow());
        notifications.show();
    }

    public static RestTemplate setupRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypes);
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    public static ContextMenu setupTableViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        contextMenu.getItems().add(deleteMenuItem);
        contextMenu.getItems().add(editMenuItem);

        return contextMenu;
    }

    public static LocalDate convertStringToLocalDate(String date) {
        LocalDate returnedDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            returnedDate = LocalDate.parse(date, formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnedDate;
    }
}
