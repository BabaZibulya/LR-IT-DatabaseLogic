package com.univ.it.gui;

import com.univ.it.table.DataBase;
import com.univ.it.table.Table;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.util.*;

public class MainWindow extends Application {
    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    private StackPane root;
    private VBox verticalLayout;
    private DataBase currentDB;
    private TabPane tabPane;

    private void initUI(Stage stage) {
        root = new StackPane();
        verticalLayout = new VBox();

        Scene scene = new Scene(root);

        initializeMenuBar();
        initializeTableTab();

        root.getChildren().add(verticalLayout);

        stage.setTitle("Database Logic");
        stage.setScene(scene);
        stage.show();
    }

    private void initializeMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuHelp = new Menu("Help");

        MenuItem newDbMenuItem = new MenuItem("New DB");
        newDbMenuItem.setOnAction(t -> createDb());
        MenuItem newDbMenuItem = new MenuItem("New Table");
        newDbMenuItem.setOnAction(t -> createTable());
        MenuItem openDbMenuItem = new MenuItem("Open DB");
        openDbMenuItem.setOnAction(t -> openDb());
        menuFile.getItems().addAll(newDbMenuItem, openDbMenuItem);

        MenuItem helpMenuItem = new MenuItem("About");
        helpMenuItem.setOnAction(t -> showHelpWindow());
        menuHelp.getItems().addAll(helpMenuItem);

        menuBar.getMenus().addAll(menuFile, menuHelp);
        verticalLayout.getChildren().add(menuBar);
    }

    private void createDb() {
        Label secondLabel = new Label("Name of data base");
        TextField dbNameTextField = new TextField();
        Button createButton = new Button("Create");

        HBox secondaryLayout = new HBox();
        secondaryLayout.getChildren().addAll(secondLabel, dbNameTextField, createButton);
        Scene secondScene = new Scene(secondaryLayout);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        createButton.setOnAction(e -> {
            if (!dbNameTextField.getText().equals("")) {
                currentDB = new DataBase(dbNameTextField.getText());
                newWindow.close();
            } else {
                showErrorMessage("Empty database name");
            }
        });

        newWindow.show();
    }

    private void createTable() {
        Label secondLabel = new Label("Name of table");
        TextField dbNameTextField = new TextField();
        Button createButton = new Button("Create");

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Option 1",
                        "Option 2",
                        "Option 3"
                );

        ComboBox comboBox = new ComboBox(options);

        HBox secondaryLayout = new HBox();
        secondaryLayout.getChildren().addAll(secondLabel, dbNameTextField);
        Scene secondScene = new Scene(secondaryLayout);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        createButton.setOnAction(e -> {
            if (!dbNameTextField.getText().equals("")) {
                currentDB = new DataBase(dbNameTextField.getText());
                newWindow.close();
            } else {
                showErrorMessage("Empty database name");
            }
        });

        newWindow.show();
    }

    private void openDb() {
        Stage newWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Database File");
        File file = fileChooser.showOpenDialog(newWindow);
        if (file != null) {
            try {
                currentDB = DataBase.readFromFile(file.getAbsolutePath());
                showDataBase();
            } catch (Exception e) {
                showErrorMessage(e.toString());
            }
        } else {
            showErrorMessage("Choose file");
        }
    }

    private void showDataBase() {
        HashMap<String, Table> tables = currentDB.getTables();
        for (HashMap.Entry<String, Table> entry : tables.entrySet()) {
            String tableName = entry.getKey();
            Table table = entry.getValue();
            Tab tab = new Tab();
            tab.setText(tableName);
            TableView tableView = new TableView();
            tab.setContent(tableView);
            tabPane.getTabs().add(tab);
            showTable(table, tableView);
        }
    }

    private void showTable(Table table, TableView tableView) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        for (int i = 0; i < table.columnNumber(); i++) {
            TableColumn col = new TableColumn(table.getColumn(i).getName());
            final int j = i;
            col.setCellValueFactory(
                    (Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
                            new SimpleStringProperty(param.getValue().get(j).toString())
            );
            tableView.getColumns().add(col);
        }

        for (int i = 0; i < table.size(); i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j = 0; j < table.getRow(i).size(); j++) {
                row.add(table.getRow(i).getAt(j).toString());
            }
            data.add(row);
        }
        tableView.setItems(data);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showHelpWindow() {
        Label secondLabel = new Label("Author: Mykola Bondarenko");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        newWindow.show();
    }

    private void initializeTableTab() {
        Tab tab = new Tab();
        tabPane = new TabPane();
        tab.setText("new tab");
        TableView tableView = new TableView();
        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
        verticalLayout.getChildren().add(tabPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
