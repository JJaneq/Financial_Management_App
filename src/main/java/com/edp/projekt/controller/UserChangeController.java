package com.edp.projekt.controller;

import com.edp.projekt.db.User;
import com.edp.projekt.DAO.UserDAO;
import com.edp.projekt.events.MainScreenRefreshEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UserChangeController extends BasicController{
    @FXML
    ChoiceBox<String> userChoiceBox;

    @FXML
    public void initialize() {
        ArrayList<User> users = UserDAO.getAllUsers();
        int currentId = ServiceManager.loadLastUserId();
        for (User user : users) {
            userChoiceBox.getItems().add(user.getUsername());
        }
        if (currentId > 0)
            userChoiceBox.setValue(UserDAO.getUser(ServiceManager.loadLastUserId()).toString());
        else
            userChoiceBox.setValue(null);
    }

    @FXML
    public void onChangeButtonClicked(ActionEvent event) {
        ServiceManager.saveLastUserId(UserDAO.getUser(userChoiceBox.getValue()).getId());
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
