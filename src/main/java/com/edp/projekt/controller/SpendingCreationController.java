package com.edp.projekt.controller;


import com.edp.projekt.DAO.CategoryDAO;
import com.edp.projekt.DAO.SpendingDAO;
import com.edp.projekt.db.Categories;
import com.edp.projekt.db.Expense;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SpendingCreationController implements BasicController{
    MainController parentController;

    @FXML
    TextField moneyTextField;
    @FXML
    DatePicker datePicker;
    @FXML
    ChoiceBox<String> categoryChoiceBox, currencyChoiceBox;
    @FXML
    TextArea descriptionTextArea;

    @FXML
    private void initialize() {
        ArrayList<Categories> categories = CategoryDAO.getAll();
        for (Categories category : categories) {
            categoryChoiceBox.getItems().add(category.toString());
        }
        categoryChoiceBox.setValue(categories.get(0).toString());
        currencyChoiceBox.getItems().add("EUR");
        currencyChoiceBox.getItems().add("USD");
        currencyChoiceBox.getItems().add("GBP");
        currencyChoiceBox.getItems().add("JPY");
        currencyChoiceBox.getItems().add("CHF");
        currencyChoiceBox.getItems().add("AUD");
        currencyChoiceBox.getItems().add("CAD");
        currencyChoiceBox.getItems().add("NZD");
        currencyChoiceBox.getItems().add("CNY");
        currencyChoiceBox.getItems().add("PLN");
        currencyChoiceBox.setValue("PLN");
    }

    @Override
    public void setParentController(MainController mainController) {
        this.parentController = mainController;
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onAddButtonClicked(ActionEvent event) {
        //TODO: dodaj walidację danych
        Expense newExpense = new Expense();
        newExpense.setPrice(Float.parseFloat(moneyTextField.getText()));
        newExpense.setDescription(descriptionTextArea.getText());
        //TODO: może być category -1
        newExpense.setCategoryId(CategoryDAO.getCategoryId(categoryChoiceBox.getValue()));
        newExpense.setUserId(ServiceManager.loadLastUserId());
        newExpense.setCurrencySymbol(currencyChoiceBox.getValue());
        newExpense.setExpenseTime(datePicker.getValue().atStartOfDay());
        SpendingDAO.addSpending(newExpense);
        parentController.updateSpendingInfoPane();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
