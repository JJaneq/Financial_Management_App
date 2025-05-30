package com.edp.projekt.controller;

import com.edp.projekt.db.User;
import com.edp.projekt.db.UserDAO;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserCreationController implements BasicController{
    @FXML
    Label warningLabel;
    @FXML
    TextField usernameText;
    @FXML
    TextField moneyText;
    @FXML
    TextField monthlyLimitText;

    MainController parentController;

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
    private void onAddUserButtonClicked(ActionEvent event) {
        if (usernameText.getText().trim().isEmpty() || moneyText.getText().trim().isEmpty()) {
            warningLabel.setVisible(true);
        }
        else {
            User newUser = new User();
            newUser.setUsername(usernameText.getText());
            //TODO: sprawdzaj czy nie ma innych wartości niż liczby
            newUser.setMoney(Float.parseFloat(moneyText.getText()));
            if (monthlyLimitText.getText().trim().isEmpty())
                newUser.setMonthLimit(0.0F);
            else
                newUser.setMonthLimit(Float.parseFloat(monthlyLimitText.getText()));
            UserDAO.addUser(newUser);
            newUser = UserDAO.getUser(newUser.getUsername());
            ServiceManager.saveLastUserId(newUser.getId());
            parentController.updateUser();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
