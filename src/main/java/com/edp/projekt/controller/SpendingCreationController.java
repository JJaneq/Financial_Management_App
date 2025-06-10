package com.edp.projekt.controller;


import com.edp.projekt.DAO.CategoryDAO;
import com.edp.projekt.DAO.TransactionDAO;
import com.edp.projekt.DAO.UserDAO;
import com.edp.projekt.db.Categories;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.db.User;
import com.edp.projekt.events.MainScreenRefreshEvent;
import com.edp.projekt.events.TransactionAddedEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class SpendingCreationController extends BasicController{
    @FXML
    TextField moneyTextField;
    @FXML
    DatePicker datePicker;
    @FXML
    ChoiceBox<String> categoryChoiceBox, currencyChoiceBox;
    @FXML
    TextArea descriptionTextArea;

    @FXML
    public void initialize() {
        super.initialize();
        ArrayList<Categories> categories = CategoryDAO.getAllByType("expense");
        for (Categories category : categories) {
            categoryChoiceBox.getItems().add(category.toString());
        }
        datePicker.setValue(LocalDate.now());
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

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onAddButtonClicked(ActionEvent event) {
        String priceText = moneyTextField.getText();
        if (StringUtils.isBlank(priceText)) {
            showError("Wprowadź kwotę.");
            return;
        }
        float price;
        try {
            price = Float.parseFloat(priceText);
            if (price <= 0) {
                showError("Kwota musi być większa od zera.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Nieprawidłowy format kwoty.");
            return;
        }
        String description = descriptionTextArea.getText();
        if (StringUtils.isBlank(description)) {
            showError("Wprowadź opis wydatku.");
            return;
        }
        String selectedCategory = categoryChoiceBox.getValue();
        int categoryId = CategoryDAO.getCategoryId(selectedCategory);
        if (categoryId == -1) {
            showError("Wybierz poprawną kategorię.");
            return;
        }

        // Tworzenie obiektu transakcji
        Transaction newExpense = new Transaction();
        newExpense.setPrice(price);
        newExpense.setDescription(description);
        newExpense.setCategoryId(categoryId);
        newExpense.setUserId(ServiceManager.loadLastUserId());
        newExpense.setCurrencySymbol(currencyChoiceBox.getValue());
        newExpense.setExpenseTime(datePicker.getValue().atStartOfDay());
        newExpense.setType("expense");

        // Zapis do bazy
        TransactionDAO.addTransaction(newExpense);
        User user = UserDAO.getUser(newExpense.getUserId());
        user.handleTransaction(newExpense);

        EventBusManager.post(new TransactionAddedEvent(newExpense));
        EventBusManager.post(new MainScreenRefreshEvent());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
