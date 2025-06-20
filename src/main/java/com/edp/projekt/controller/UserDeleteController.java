package com.edp.projekt.controller;

import com.edp.projekt.DAO.UserDAO;
import com.edp.projekt.events.MainScreenRefreshEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UserDeleteController extends BasicController{
    @FXML
    private void onDeleteButtonClicked(ActionEvent event) {
        UserDAO.deleteUser(ServiceManager.loadLastUserId());
        ServiceManager.saveLastUserId(-1);

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
