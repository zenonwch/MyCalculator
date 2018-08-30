package com.vaz.projects.calculator.view.menu;

import com.vaz.projects.calculator.view.hotkeys.HotKeyWindow;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public final class MenuFactory {
    private static final String MENU_HELP = "Help";
    private static final String KEYBOARD_SHORTCUTS = "Keyboard Shortcuts";

    private MenuFactory() {
    }

    public static Menu createHelpMenu(final Stage rootStage) {
        final Menu menu = new Menu(MENU_HELP);

        final MenuItem itemShortcuts = createMenuItem(HotKeyWindow.class, KEYBOARD_SHORTCUTS, rootStage);
        final MenuItem itemAbout = new MenuItem("About MyCalculator");

        menu.getItems().addAll(itemShortcuts, itemAbout);
        return menu;
    }

    private static MenuItem createMenuItem(final Class<? extends Stage> clazz,
                                           final String name, final Stage root) {
        final MenuItem menuItem = new MenuItem(name);
        Stage testStage;
        try {
            testStage = (Stage) clazz.getMethod("create").invoke(null);
        } catch (final Exception ignore) {
            System.out.println("ERROR");
            testStage = new Stage();
        }
        final Stage stage = testStage;

        menuItem.setOnAction(event -> {
            final double newWinX = root.getX() + root.getWidth() - 50.0;
            final double newWinY = root.getY();

            stage.setTitle(name);
            stage.setX(newWinX);
            stage.setY(newWinY);
            stage.requestFocus();
            stage.show();
        });

        return menuItem;
    }
}
