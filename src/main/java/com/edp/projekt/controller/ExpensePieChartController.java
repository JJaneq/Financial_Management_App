package com.edp.projekt.controller;

import com.edp.projekt.DAO.CategoryDAO;
import com.edp.projekt.DAO.TransactionDAO;
import com.edp.projekt.components.ExpensePieChart;
import com.edp.projekt.db.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

public class ExpensePieChartController implements BasicController {

    @FXML private TextField categoryField;
    @FXML private TextField valueField;
    @FXML private VBox chartContainer;

    private ExpensePieChart expensePieChart;
    MainController parentController;

    @FXML
    public void initialize() {
        expensePieChart = new ExpensePieChart();
        chartContainer.getChildren().add(expensePieChart);
        ArrayList<Transaction> expenses = TransactionDAO.getAllTransactions(1);
        for (Transaction transaction : expenses) {
            if (Objects.equals(transaction.getType(), "expense")) {
                expensePieChart.addExpense(CategoryDAO.getById(transaction.getCategoryId()).toString(), transaction.getPrice());
            }
        }
    }

    @FXML
    private void onAddExpense() {
        String category = categoryField.getText();
        String valueText = valueField.getText();
        try {
            float value = Float.parseFloat(valueText);
            expensePieChart.addExpense(category, value);
            categoryField.clear();
            valueField.clear();
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowa kwota.");
        }
    }

    @Override
    public void setParentController(MainController mainController) {
        this.parentController = mainController;
    }
}
