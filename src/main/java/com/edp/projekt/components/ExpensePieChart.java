package com.edp.projekt.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class ExpensePieChart extends VBox {

    private final PieChart pieChart;
    private final Map<String, Float> categoryMap;
    private final ObservableList<PieChart.Data> pieChartData;

    public ExpensePieChart() {
        this.categoryMap = new HashMap<>();
        this.pieChartData = FXCollections.observableArrayList();
        this.pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Wydatki wg kategorii");

        this.getChildren().add(pieChart);
        this.setMinSize(400, 400);
    }

    public void addExpense(String category, Float value) {
        if (category == null || value == null || value <= 0) return;

        // Aktualizacja wartości kategorii
        categoryMap.merge(category, value, Float::sum);
        refreshChart();
    }

    private void refreshChart() {
        pieChartData.clear();

        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }
}

