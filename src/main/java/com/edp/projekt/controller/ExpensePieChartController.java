package com.edp.projekt.controller;

import com.edp.projekt.DAO.CategoryDAO;
import com.edp.projekt.DAO.TransactionDAO;
import com.edp.projekt.components.ExpensePieChart;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.events.TransactionAddedEvent;
import com.edp.projekt.service.EventBusManager;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

public class ExpensePieChartController extends BasicController {
    @FXML private TextField categoryField;
    @FXML private TextField valueField;
    @FXML private VBox chartContainer;

    private ExpensePieChart expensePieChart;

    @FXML
    public void initialize() {
        EventBusManager.register(this);
        expensePieChart = new ExpensePieChart();
        chartContainer.getChildren().add(expensePieChart);
        updateChart();
    }

    @Subscribe
    public void onRefresh(TransactionAddedEvent event) {
        updateChart();
    }

    private void updateChart() {
        ArrayList<Transaction> expenses = TransactionDAO.getAllTransactions(1);
        for (Transaction transaction : expenses) {
            if (Objects.equals(transaction.getType(), "expense")) {
                expensePieChart.addExpense(CategoryDAO.getById(transaction.getCategoryId()).toString(), transaction.getPrice());
            }
        }
    }
}
