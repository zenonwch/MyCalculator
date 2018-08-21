package com.vaz.projects.calculator;

import com.vaz.projects.calculator.controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;

import java.net.URL;
import java.util.Set;
import java.util.function.Predicate;

import static com.vaz.projects.calculator.Main.GUI_FXML;
import static com.vaz.projects.calculator.Main.STYLESHEET;

public class MyApplicationTest extends ApplicationTest {

    private Controller controller;

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public void start(final Stage stage) throws Exception {
        final URL resourceUrl = getClass().getResource("../" + GUI_FXML);
        final FXMLLoader loader = new FXMLLoader(resourceUrl);

        final Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(STYLESHEET);
        stage.show();

        controller = loader.getController();
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    protected <T extends Node> T find(final String query) {
        /* TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).query();
    }

    protected Set<Button> findAllButtons() {
        return lookup(".button").queryAllAs(Button.class);
    }

    protected <T extends Node> T findButton(final String text) {
        final Predicate<Button> getButtonByText = b -> text.equals(b.getText());
        return from(lookup(".button")).lookup(getButtonByText).query();
    }

    protected Controller getController() {
        return controller;
    }
}