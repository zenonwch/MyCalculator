package com.vaz.projects.calculator;

import com.vaz.projects.calculator.controller.Controller;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Set;
import java.util.function.Predicate;

import static com.vaz.projects.calculator.Main.CONTROLLER_PROPERTY;

public class MyApplicationTest extends ApplicationTest {
    private Controller controller;

    @BeforeEach
    protected void setUp() throws Exception {
        ApplicationTest.launch(Main.class);

        final Stage stage = (Stage) findButton("0").getScene().getWindow();
        controller = (Controller) stage.getProperties().get(CONTROLLER_PROPERTY);

        // Test application will appear on the top right conner of the screen
        final Rectangle2D visibleScreen = Screen.getPrimary().getVisualBounds();
        stage.setX(visibleScreen.getWidth() - stage.getWidth() - 10);
        stage.setY(10);
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