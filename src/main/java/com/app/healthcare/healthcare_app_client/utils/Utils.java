package com.app.healthcare.healthcare_app_client.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

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
}
