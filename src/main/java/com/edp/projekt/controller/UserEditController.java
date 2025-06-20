package com.edp.projekt.controller;

import com.edp.projekt.db.User;
import com.edp.projekt.DAO.UserDAO;
import com.edp.projekt.events.MainScreenRefreshEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditController extends BasicController{
    @FXML
    private Label profileLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField moneyTextField;
    @FXML
    private TextField monthlyLimitTextField;

    @FXML
    public void initialize() {
        super.initialize();
        User currentUser = UserDAO.getUser(ServiceManager.loadLastUserId());
        profileLabel.setText("Profil: " + currentUser.toString());
        usernameTextField.setText(currentUser.getUsername());
        moneyTextField.setText(currentUser.getMoney() + "");
        monthlyLimitTextField.setText(currentUser.getMonthLimit() + "");
    }

    @FXML
    private void onSaveButtonClicked(ActionEvent event) {
        User newUser = new User();
        newUser.setUsername(usernameTextField.getText());
        newUser.setMoney(Float.parseFloat(moneyTextField.getText()));
        newUser.setMonthLimit(Float.parseFloat(monthlyLimitTextField.getText()));
        UserDAO.updateUser(ServiceManager.loadLastUserId(), newUser);

        EventBusManager.post(new MainScreenRefreshEvent());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
