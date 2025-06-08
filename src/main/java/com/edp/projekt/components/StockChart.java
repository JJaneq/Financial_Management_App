package com.edp.projekt.components;

import com.edp.projekt.DAO.StockDAO;
import com.edp.projekt.DAO.StockPriceDAO;
import com.edp.projekt.db.Stock;
import com.edp.projekt.db.StockPrice;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StockChart extends VBox {

    private final LineChart<String, Number> lineChart;
    private final XYChart.Series<String, Number> closeSeries, openSeries, highSeries, lowSeries;
    private final Label titleLabel;
    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;
    private final ScrollPane scrollPane;
    private final ChoiceBox<String> stockSymbolBox;
    private final ChoiceBox<String> stockLenghtBox ;
    private final AnchorPane buttonAnchorPane;
    private final HBox checkBoxRow, choiceBoxRow;
    private final CheckBox openCheckBox, highCheckBox, lowCheckBox, closeCheckBox;

    private String stockSymbol;

    public StockChart() {
        this("Brak danych");
    }

    public StockChart(String stockSymbol) {
        this.stockSymbol = stockSymbol;

        titleLabel = new Label("Wykres akcji: " + this.stockSymbol);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        xAxis = new CategoryAxis();
        xAxis.setLabel("Czas");
        xAxis.setAutoRanging(true);
        xAxis.setTickLabelRotation(90);

        yAxis = new NumberAxis();
        yAxis.setLabel("Cena");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setMinHeight(350);

        closeSeries = new XYChart.Series<>();
        openSeries = new XYChart.Series<>();
        highSeries = new XYChart.Series<>();
        lowSeries = new XYChart.Series<>();
        lineChart.getData().add(openSeries);
        lineChart.getData().add(highSeries);
        lineChart.getData().add(lowSeries);
        lineChart.getData().add(closeSeries);

        checkBoxRow = new HBox(10); // odstęp między checkboxami
        openCheckBox = new CheckBox("Open");
        highCheckBox = new CheckBox("High");
        lowCheckBox = new CheckBox("Low");
        closeCheckBox = new CheckBox("Close");

        closeCheckBox.setSelected(true); // domyślnie aktywne

        checkBoxRow.getChildren().addAll(openCheckBox, highCheckBox, lowCheckBox, closeCheckBox);

        scrollPane = new ScrollPane(lineChart);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(false);  // <- możesz wyłączyć przewijanie
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        stockSymbolBox = new ChoiceBox<>();
        for (Stock stock: StockDAO.getAllStocks()) {
            String symbol = stock.getStockSymbol();
            stockSymbolBox.getItems().add(symbol);
        }
        stockSymbolBox.getSelectionModel().select(stockSymbol);

        stockLenghtBox = new ChoiceBox<>();
        stockLenghtBox.getItems().add("30 dni");
        stockLenghtBox.getItems().add("60 dni");
        stockLenghtBox.getItems().add("120 dni");
        stockLenghtBox.getItems().add("365 dni");
        stockLenghtBox.getItems().add("Od początku");
        stockLenghtBox.getSelectionModel().select("30 dni");

        choiceBoxRow = new HBox(10);
        choiceBoxRow.getChildren().addAll(stockSymbolBox, stockLenghtBox);

        AnchorPane.setTopAnchor(checkBoxRow, 10.0);
        AnchorPane.setLeftAnchor(checkBoxRow, 10.0);

        AnchorPane.setTopAnchor(choiceBoxRow, 45.0);
        AnchorPane.setLeftAnchor(choiceBoxRow, 10.0);

        buttonAnchorPane = new AnchorPane();
        buttonAnchorPane.getChildren().addAll(checkBoxRow, choiceBoxRow);
        buttonAnchorPane.setPrefHeight(80);

        this.getChildren().addAll(titleLabel, scrollPane, buttonAnchorPane);
        this.setSpacing(10);

        // Pozwól rosnąć
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

        //Listeners
        stockSymbolBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });

        stockLenghtBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });
        closeCheckBox.setOnAction(event -> updateChart());
        openCheckBox.setOnAction(event -> updateChart());
        highCheckBox.setOnAction(event -> updateChart());
        lowCheckBox.setOnAction(event -> updateChart());
    }


    public void setSymbol(String symbol) {
        this.titleLabel.setText("Wykres akcji: " + symbol);
        this.stockSymbol = symbol;
        this.updateData(120);
    }

    public void updateData() {
        ArrayList<StockPrice> stockPrices = StockPriceDAO.getStockData(this.stockSymbol);

        closeSeries.getData().clear();
        highSeries.getData().clear();
        lowSeries.getData().clear();
        openSeries.getData().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");

        for (StockPrice stockPrice : stockPrices) {
            String dateStr = stockPrice.getStockTime().format(formatter);
            if (closeCheckBox.isSelected()) closeSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getClose()));
            if (highCheckBox.isSelected()) highSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getHigh()));
            if (lowCheckBox.isSelected()) openSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getLow()));
            if (openCheckBox.isSelected()) lowSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getOpen()));
        }

        if (closeCheckBox.isSelected()) {
            if (!lineChart.getData().contains(closeSeries)) lineChart.getData().add(closeSeries);
        } else {
            lineChart.getData().remove(closeSeries);
        }

        if (highCheckBox.isSelected()) {
            if (!lineChart.getData().contains(highSeries)) lineChart.getData().add(highSeries);
        } else {
            lineChart.getData().remove(highSeries);
        }

        if (openCheckBox.isSelected()) {
            if (!lineChart.getData().contains(openSeries)) lineChart.getData().add(openSeries);
        } else {
            lineChart.getData().remove(openSeries);
        }

        if (lowCheckBox.isSelected()) {
            if (!lineChart.getData().contains(lowSeries)) lineChart.getData().add(lowSeries);
        } else {
            lineChart.getData().remove(lowSeries);
        }

        // Dopasowanie szerokości do liczby danych (np. 15px na punkt)
        int dataSize = stockPrices.size();
        int pointWidth = 12; // możesz zmienić
        scrollPane.setFitToWidth(true); // <--- kluczowe!
    }

    private void updateChart() {
        String selectedSymbol = stockSymbolBox.getSelectionModel().getSelectedItem();
        if (selectedSymbol == null) {
            selectedSymbol = this.stockSymbol;
        }
        setSymbol(selectedSymbol);
        String selectedLength = stockLenghtBox.getSelectionModel().getSelectedItem();
        int days = -1;
        days = switch (selectedLength) {
            case "30 dni" -> 30;
            case "60 dni" -> 60;
            case "120 dni" -> 120;
            case "365 dni" -> 365;
            default -> days;
        };

        boolean showOpen = openCheckBox.isSelected();
        boolean showHigh = highCheckBox.isSelected();
        boolean showLow = lowCheckBox.isSelected();
        boolean showClose = closeCheckBox.isSelected();

        if (days > 0)
            updateData(days);
        else
            updateData();
    }

    public void updateData(int days) {
        ArrayList<StockPrice> stockPrices = StockPriceDAO.getStockData(this.stockSymbol, days);

        closeSeries.getData().clear();
        highSeries.getData().clear();
        lowSeries.getData().clear();
        openSeries.getData().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");

        for (StockPrice stockPrice : stockPrices) {
            String dateStr = stockPrice.getStockTime().format(formatter);
            if (closeCheckBox.isSelected()) closeSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getClose()));
            if (highCheckBox.isSelected()) highSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getHigh()));
            if (openCheckBox.isSelected()) openSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getLow()));
            if (lowCheckBox.isSelected()) lowSeries.getData().add(new XYChart.Data<>(dateStr, stockPrice.getOpen()));
        }

        if (closeCheckBox.isSelected()) {
            if (!lineChart.getData().contains(closeSeries)) lineChart.getData().add(closeSeries);
        } else {
            lineChart.getData().remove(closeSeries);
        }

        if (highCheckBox.isSelected()) {
            if (!lineChart.getData().contains(highSeries)) lineChart.getData().add(highSeries);
        } else {
            lineChart.getData().remove(highSeries);
        }

        if (openCheckBox.isSelected()) {
            if (!lineChart.getData().contains(openSeries)) lineChart.getData().add(openSeries);
        } else {
            lineChart.getData().remove(openSeries);
        }

        if (lowCheckBox.isSelected()) {
            if (!lineChart.getData().contains(lowSeries)) lineChart.getData().add(lowSeries);
        } else {
            lineChart.getData().remove(lowSeries);
        }

        // Dopasowanie szerokości do liczby danych (np. 15px na punkt)
        int dataSize = stockPrices.size();
        int pointWidth = 12; // możesz zmienić
        scrollPane.setFitToWidth(true); // <--- kluczowe!

    }
}
