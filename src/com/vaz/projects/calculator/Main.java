package com.vaz.projects.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    static final String GUI_FXML = "./view/calc.fxml";
    static final String STYLESHEET = "/styles/calc.css";
    private static final String APP_NAME = "Calculator";

    @Override
    public void start(final Stage primaryStage) throws Exception {
        final URL resourceUrl = getClass().getResource(GUI_FXML);
        final Parent root = FXMLLoader.load(resourceUrl);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(APP_NAME);
        primaryStage.getScene().getStylesheets().add(STYLESHEET);

        final MenuBar menuBar = new MenuBar();
        final Menu menuView = new Menu("Veiw");
        final Menu menuEdit = new Menu("Edit");
        final Menu menuHelp = new Menu("Help");

        menuBar.getMenus().addAll(menuView, menuEdit, menuHelp);
        ((VBox) root).getChildren().add(0, menuBar);

        final InputStream iconResource = getClass().getResourceAsStream("/icons/calculator_icon.png");
        final Image icon = new Image(iconResource);
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}
