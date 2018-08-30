package com.vaz.projects.calculator.view.hotkeys;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class HotKeyWindow extends Stage {
    private static final String HOT_KEYS_FXML = "./hot_keys.fxml";

    private HotKeyWindow() {
    }

    public static HotKeyWindow create() {
        final HotKeyWindow instance = new HotKeyWindow();
        final URL resourceUrl = instance.getClass().getResource(HOT_KEYS_FXML);
        final FXMLLoader loader = new FXMLLoader(resourceUrl);

        try {
            final Parent root = loader.load();
            instance.setScene(new Scene(root, 300, 510));
            final InputStream iconResource = instance.getClass().getResourceAsStream("/icons/fully_transparent.png");
            final Image icon = new Image(iconResource);

            instance.getIcons().add(icon);
        } catch (final IOException ignored) {
            System.out.println("Something went wrong!!!");
        }

        instance.setMaxHeight(545);
        return instance;
    }
}
