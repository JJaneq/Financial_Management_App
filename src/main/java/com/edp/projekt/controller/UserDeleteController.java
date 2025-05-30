package com.edp.projekt.controller;

import com.edp.projekt.db.User;
import com.edp.projekt.db.UserDAO;
import com.edp.projekt.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UserDeleteController implements BasicController{
    MainController parentController;

    @Override
    public void setParentController(MainController mainController) {
        this.parentController = mainController;
    }

    @FXML
    private void onDeleteButtonClicked(ActionEvent event) {
        UserDAO.deleteUser(ServiceManager.loadLastUserId());
        ServiceManager.saveLastUserId(-1);

        parentController.updateUser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
