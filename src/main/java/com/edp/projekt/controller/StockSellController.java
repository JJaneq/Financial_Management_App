package com.edp.projekt.controller;

import com.edp.projekt.DAO.*;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.db.UserStock;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StockSellController implements BasicController{
    MainController parentController;
    Map<Integer, String> userStockValues;
    @FXML
    ChoiceBox<String> stockChoiceBox;
    @FXML
    Spinner<Integer> quantitySpinner;
    @FXML
    Label currentPriceLabel, purchasePriceLabel;

    @Override
    public void setParentController(MainController mainController) {
        this.parentController = mainController;
    }

    @FXML
    public void initialize() {
        this.userStockValues = new HashMap<>();
        ArrayList<UserStock> ownedStocks = UserStockDAO.getUserStocks(ServiceManager.loadLastUserId());
        for (UserStock userStock : ownedStocks) {
            stockChoiceBox.getItems().add(userStock.toString());
            userStockValues.put(userStock.getId(), userStock.toString());
        }

        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));

        stockChoiceBox.getSelectionModel().selectFirst();
        stockChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           selectedStockChanged();
        });
    }

    @FXML
    public void onSellButtonClicked(ActionEvent event) {
        String selectedStockString = stockChoiceBox.getValue();
        int selectedId = -1;

        for (Map.Entry<Integer, String> entry : userStockValues.entrySet()) {
            if (entry.getValue().equals(selectedStockString)) {
                selectedId = entry.getKey();
                break;
            }
        }
        if (selectedId == -1) {
            return;
        }

        UserStock userStock = UserStockDAO.getUserStockById(selectedId);
        Transaction transaction = new Transaction();
        transaction.setType("income");
        transaction.setExpenseTime(LocalDateTime.now());
        transaction.setUserId(ServiceManager.loadLastUserId());
        transaction.setCategoryId(CategoryDAO.getCategoryId("Dochód z inwestycji"));
        transaction.setCurrencySymbol(userStock.getCurrency());
        transaction.setDescription(StockDAO.getStockSymbol(userStock.getStockId()) + " : " + quantitySpinner.getValue());
        transaction.setPrice(quantitySpinner.getValue() * StockPriceDAO.getLatestPrice(userStock.getStockId()));
        TransactionDAO.addTransaction(transaction);

        if (quantitySpinner.getValue() == userStock.getQuantity()) {
            UserStockDAO.deleteUserStock(userStock);
        } else if (quantitySpinner.getValue() < userStock.getQuantity()) {
            userStock.setQuantity(userStock.getQuantity() - quantitySpinner.getValue());
            UserStockDAO.editUserStock(userStock);
        }

        parentController.updateMainScreen();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void selectedStockChanged() {
        //FIXME: ciekawe co się stanie jak będziemy mieć 2 takie same ciągi wartości?
        String selectedStockString = stockChoiceBox.getValue();
        int selectedId = -1;

        for (Map.Entry<Integer, String> entry : userStockValues.entrySet()) {
            if (entry.getValue().equals(selectedStockString)) {
                selectedId = entry.getKey();
                break;
            }
        }

        if (selectedId != -1) {
            UserStock selectedStock = UserStockDAO.getUserStockById(selectedId);
            if (selectedStock != null) {
                purchasePriceLabel.setText(selectedStock.getPurchasePrice() + "");
                currentPriceLabel.setText(StockPriceDAO.getLatestPrice(selectedStock.getStockId()) + "");
                quantitySpinner.getValueFactory().setValue(1);
                quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selectedStock.getQuantity()));
            }
        }
    }

}
