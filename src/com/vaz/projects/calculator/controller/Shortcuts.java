package com.vaz.projects.calculator.controller;

import com.vaz.projects.calculator.model.HotKey;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class Shortcuts implements Initializable {
    private static final String SOURCE = "shortcuts.csv";
    private static final String DECIMETER = ",";

    @FXML
    private TableView<HotKey> tableView;
    @FXML
    private TableColumn<HotKey, String> keys;
    @FXML
    private TableColumn<HotKey, String> actions;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        keys.setCellValueFactory(new PropertyValueFactory<>("Key"));
        actions.setCellValueFactory(new PropertyValueFactory<>("Action"));

        final ObservableList<HotKey> hotKeys = getHotKeysList();

        tableView.getItems().setAll(hotKeys);
    }

    private ObservableList<HotKey> getHotKeysList() {
        final ObservableList<HotKey> hotKeyList = FXCollections.observableArrayList();
        final URL filePath = getClass().getClassLoader().getResource(SOURCE);

        try (final Stream<String> stream = Files.lines(Paths.get(filePath.toURI()))) {
            stream.forEach(line -> {
                final String[] fields = line.split(DECIMETER);
                hotKeyList.add(new HotKey(fields[0], fields[1]));
            });
        } catch (final IOException | URISyntaxException ignored) {
            System.out.println("Something went wrong");
        }

        return hotKeyList;
    }
}
