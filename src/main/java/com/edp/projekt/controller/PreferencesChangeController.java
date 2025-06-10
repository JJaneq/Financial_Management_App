package com.edp.projekt.controller;

import com.edp.projekt.DAO.StockDAO;
import com.edp.projekt.events.ChangePreferredStockChartEvent;
import com.edp.projekt.events.ChangeResolutionEvent;
import com.edp.projekt.events.ChangeThemeEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import com.google.common.eventbus.Subscribe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

public class PreferencesChangeController extends BasicController{
    @FXML
    ChoiceBox<String> colorSchemeChoiceBox, actionSymbolChoiceBox;
    @FXML
    ComboBox<String> resolutionComboBox;

    Dimension screenSize;
    Dimension ogResolution;
    String ogTheme;
    String ogStockSymbol;

    @FXML
    public void initialize() {
        super.initialize();
        ogResolution = ServiceManager.getPreferredResolution();
        ogTheme = ServiceManager.getPreferredTheme();
        ogStockSymbol = ServiceManager.getPreferredStock();

        //Loading stock symbols
        StockDAO.getAllStocks().forEach(stock -> {actionSymbolChoiceBox.getItems().add(stock.getStockSymbol());});
        String currentStock = ServiceManager.getPreferredStock();
        if (!currentStock.equals("Brak danych"))
            actionSymbolChoiceBox.setValue(ServiceManager.getPreferredStock());

        //Loading themes
        String themeFolderPath = "src/main/resources/themes/";
        File themeFolder = new File(themeFolderPath);
        if (themeFolder.exists() && themeFolder.isDirectory()) {
            File[] files = themeFolder.listFiles((dir, name) -> name.endsWith(".css"));
            if (files != null) {
                for (File file : files) {
                    colorSchemeChoiceBox.getItems().add(file.getName());
                }
            }
        }

        //Setting resolution
        screenSize = ServiceManager.getPreferredResolution();
        resolutionComboBox.setValue(screenSize.width + "x" + screenSize.height);

        //Listeners
        resolutionComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String[] dimensions = newValue.split("x");
            Dimension newResolution = new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
            EventBusManager.post(new ChangeResolutionEvent(newResolution));
        });

        colorSchemeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            EventBusManager.post(new ChangeThemeEvent(newValue));
        });

        actionSymbolChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            EventBusManager.post(new ChangePreferredStockChartEvent(newValue));
        });
    }

    @FXML void handleSave(ActionEvent event) {
        String[] dimensions = resolutionComboBox.getValue().split("x");
        Dimension newResolution = new Dimension(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]));
        String newTheme = colorSchemeChoiceBox.getValue();
        String newStockSymbol = actionSymbolChoiceBox.getValue();

        ServiceManager.setNewPreferences(newResolution, newTheme, newStockSymbol);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        EventBusManager.post(new ChangeResolutionEvent(ogResolution));
        EventBusManager.post(new ChangeThemeEvent(ogTheme));
        EventBusManager.post(new ChangePreferredStockChartEvent(ogStockSymbol));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
