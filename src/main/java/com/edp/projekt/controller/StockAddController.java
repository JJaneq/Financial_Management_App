package com.edp.projekt.controller;

import com.edp.projekt.DAO.*;
import com.edp.projekt.db.Stock;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.db.UserStock;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class StockAddController implements BasicController{
    MainController parentController;
    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private TextField symbolField, priceField;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> currencyComboBox;

    @Override
    public void setParentController(MainController mainController) {
        this.parentController = mainController;
    }

    @FXML
    private void initialize() {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));
        currencyComboBox.getItems().add("EUR");
        currencyComboBox.getItems().add("USD");
        currencyComboBox.getItems().add("GBP");
        currencyComboBox.getItems().add("JPY");
        currencyComboBox.getItems().add("CHF");
        currencyComboBox.getItems().add("AUD");
        currencyComboBox.getItems().add("CAD");
        currencyComboBox.getItems().add("NZD");
        currencyComboBox.getItems().add("CNY");
        currencyComboBox.getItems().add("PLN");
        currencyComboBox.setValue("USD");
    }

    @FXML
    public void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onAddButtonClicked(ActionEvent event) throws IOException, InterruptedException {
        String symbol = symbolField.getText().trim();
        String priceText = priceField.getText().trim();

        if (symbol.isEmpty() || priceText.isEmpty()) {
            errorLabel.setText("Uzupełnij wszystkie pola.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        float price;
        try {
            price = Float.parseFloat(priceText);
        } catch (NumberFormatException e) {
            errorLabel.setText("Nieprawidłowa cena.");
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        Stock stock = StockDAO.getStock(symbol);

        if (stock == null) {
            if (StockDAO.addStock(symbol)) {
                stock = StockDAO.getStock(symbol);
            } else {
                errorLabel.setText("Brak danych o akcjach: " + symbol);
                errorLabel.setVisible(true);
                errorLabel.setManaged(true);
                return;
            }
        }

        if (stock != null) {
            UserStock userStock = new UserStock(
                    ServiceManager.loadLastUserId(),
                    stock.getId(),
                    price,
                    quantitySpinner.getValue(),
                    currencyComboBox.getValue()
            );
            UserStockDAO.addUserStock(userStock);

            Transaction transaction = new Transaction();
            transaction.setType("expense");
            transaction.setExpenseTime(LocalDateTime.now());
            transaction.setUserId(ServiceManager.loadLastUserId());
            transaction.setCategoryId(CategoryDAO.getCategoryId("Inwestycje"));
            transaction.setCurrencySymbol(userStock.getCurrency());
            transaction.setDescription(StockDAO.getStockSymbol(userStock.getStockId()) + " : " + quantitySpinner.getValue());
            transaction.setPrice(quantitySpinner.getValue() * StockPriceDAO.getLatestPrice(userStock.getStockId()));
            TransactionDAO.addTransaction(transaction);

            parentController.updateMainScreen();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

}
