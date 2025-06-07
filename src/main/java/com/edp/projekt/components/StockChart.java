package com.edp.projekt.components;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.List;

public class StockChart extends VBox {

    private final LineChart<Number, Number> lineChart;
    private final XYChart.Series<Number, Number> series;
    private final Label titleLabel;

    public StockChart() {
        this("Brak danych");
    }

    public StockChart(String stockSymbol) {
        // Etykieta tytułu
        titleLabel = new Label("Wykres akcji: " + stockSymbol);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Oś X: Czas (np. w minutach lub dniach)
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Czas");

        // Oś Y: Cena akcji
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Cena");

        // LineChart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        lineChart.setCreateSymbols(false);
        lineChart.setMinHeight(400);

        // Dane
        series = new XYChart.Series<>();
        lineChart.getData().add(series);

        // Dodanie elementów do komponentu
        this.getChildren().addAll(titleLabel, lineChart);
        this.setSpacing(10);
    }

    /**
     * Aktualizuje dane wykresu na podstawie nowej listy punktów.
     * @param prices Lista cen (np. ceny akcji w kolejnych punktach czasu)
     */
    public void updateData(List<Double> prices) {
        series.getData().clear();
        for (int i = 0; i < prices.size(); i++) {
            series.getData().add(new XYChart.Data<>(i, prices.get(i)));
        }
    }
}
