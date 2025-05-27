package com.edp.projekt.controller;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.User;
import com.edp.projekt.db.UserDAO;
import com.edp.projekt.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class MainController {
    @FXML
    private Label helloLabel;

    @FXML
    private void initialize() {
        updateUser();
    }

    @FXML
    private void onAddUser() throws IOException {
        System.out.println("Adding user");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/edp/projekt/user-creation-view.fxml"));
        Parent root = loader.load();

        UserCreationController userCreationController = loader.getController();
        userCreationController.setParentController(this);

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.showAndWait();
    }

    public void updateUser() {
        int currentUserId = ServiceManager.loadLastUserId();
        if (currentUserId > 0) {
            User currrentUser = UserDAO.getUser(currentUserId);
            helloLabel.setText(currrentUser.getUsername() + ", witaj!");
        }
    }
}

