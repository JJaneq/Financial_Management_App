package com.edp.projekt.controller;

import com.edp.projekt.DAO.SpendingDAO;
import com.edp.projekt.db.Expense;
import com.edp.projekt.db.User;
import com.edp.projekt.DAO.UserDAO;
import com.edp.projekt.service.ServiceManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    @FXML
    private Label helloLabel, spendingLabel, moneyLabel;
    @FXML
    private MenuItem menuDelete, menuEdit;
    @FXML
    private VBox expensesVBox;


    @FXML
    private void initialize() {
        updateUserInfoPane();
        updateSpendingInfoPane();
    }

    @FXML
    private void onAddUser() throws IOException {
        UserCreationController controller = createView("user-creation-view");
    }

    @FXML
    private void onChangeUser() throws IOException {
        UserChangeController controller = createView("user-change-view");
    }

    @FXML
    private void onDeleteUser() throws IOException {
        UserDeleteController controller = createView("user-delete-view");
    }

    @FXML
    private void onEditUser() throws IOException {
        UserEditController controller = createView("user-edit-view");
    }

    @FXML
    private void onAddSpendingButtonClicked() throws IOException {
        SpendingCreationController controller = createView("spending-creation-view");
    }

    public void updateUserInfoPane() {
        int currentUserId = ServiceManager.loadLastUserId();
        if (currentUserId > 0) {
            User currentUser = UserDAO.getUser(currentUserId);
            helloLabel.setText(currentUser.getUsername() + ", witaj!");
            moneyLabel.setText(currentUser.getMoney() + "");
            if (currentUser.getMonthLimit() > 0.0)
                spendingLabel.setText("Wydatki w tym miesiącu: " + currentUser.getCurrentMonthSpendings()
                        + "/" + currentUser.getMonthLimit() + "PLN");
            else
                spendingLabel.setText("Wydatki w tym miesiącu: " + currentUser.getCurrentMonthSpendings() + "PLN");
            spendingLabel.setVisible(true);
            moneyLabel.setVisible(true);
            menuDelete.setVisible(true);
            menuEdit.setVisible(true);
        } else {
            helloLabel.setText("Wybierz istniejący profil lub utwórz nowy");
            spendingLabel.setVisible(false);
            moneyLabel.setVisible(false);
            menuDelete.setVisible(false);
            menuEdit.setVisible(false);
        }
    }

    public void updateSpendingInfoPane() {
        ArrayList<Expense> expenses = SpendingDAO.getSpendings(1);
        VBox.setMargin(expensesVBox, new Insets(20, 0, 20, 0));
        expensesVBox.setSpacing(15);
        expensesVBox.getChildren().clear();
        if (expenses.isEmpty()) {
            Label label = new Label("Brak wydatków w ostatnim miesiącu :)");
            label.setStyle("-fx-font-size: 16px");
            expensesVBox.getChildren().add(label);
        }
        else {
            for (Expense expense : expenses) {
                Label label = new Label(expense.toString());
                label.setStyle("-fx-font-size: 16px");
                expensesVBox.getChildren().add(label);
            }
        }
    }

    private <T extends BasicController> T createView(String loaderView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/edp/projekt/" + loaderView + ".fxml"));
        Parent root = loader.load();

        T controller = loader.getController();
        controller.setParentController(this);

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.initStyle(StageStyle.UNDECORATED);
        newStage.showAndWait();

        return controller;
    }
}

