package com.edp.projekt.controller;

import com.edp.projekt.db.DatabaseConnector;
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
    private Label statusLabel;

    @FXML
    private void initialize() {
        try (Connection conn = DatabaseConnector.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version()");

            if (rs.next()) {
                //statusLabel.setText("Connected to PostgreSQL: " + rs.getString(1));
            }
        } catch (Exception e) {
            //statusLabel.setText("Connection error: " + e.getMessage());
            System.out.print("Connection error: " + e.getMessage());
        }
    }

    @FXML
    private void onAddUser() throws IOException {
        System.out.println("Adding user");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-creation-view.fxml")));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.show();
    }
}

