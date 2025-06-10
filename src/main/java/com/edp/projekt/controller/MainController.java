package com.edp.projekt.controller;

import com.edp.projekt.DAO.*;
import com.edp.projekt.components.BudgetIndicator;
import com.edp.projekt.components.StockChart;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.db.User;
import com.edp.projekt.db.UserStock;
import com.edp.projekt.events.ChangePreferredStockChartEvent;
import com.edp.projekt.events.ChangeResolutionEvent;
import com.edp.projekt.events.ChangeThemeEvent;
import com.edp.projekt.events.MainScreenRefreshEvent;
import com.edp.projekt.external_api.FinancialApi;
import com.edp.projekt.external_api.NbpApiClient;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import com.google.common.eventbus.Subscribe;
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

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    @FXML
    private Stage primaryStage;
    @FXML
    private Label helloLabel, spendingLabel, moneyLabel;
    @FXML
    private MenuItem menuDelete, menuEdit;
    @FXML
    private VBox expensesVBox, stocksVBox;
    @FXML
    private BudgetIndicator budgetIndicator;
    @FXML
    private StockChart stockChart;


    @FXML
    private void initialize() {
        EventBusManager.register(this);
        updateMainScreen();
        stockChart.setSymbol(ServiceManager.getPreferredStock());
        if (ServiceManager.getPreferredStock().equals("Brak danych")) stockChart.updateChart();
        StockPriceDAO.updateStockPrices();
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
        changeTheme(ServiceManager.getPreferredTheme());
        changeStockPanel(ServiceManager.getPreferredStock());
        changeResolution(ServiceManager.getPreferredResolution());
    }

    @Subscribe
    public void onRefresh(MainScreenRefreshEvent event) {
        updateMainScreen();
    }

    @Subscribe
    public void onResolutionChange(ChangeResolutionEvent event) {
        changeResolution(event.getResolution());
    }

    @Subscribe
    public void onChangePreferredStockChart(ChangePreferredStockChartEvent event) {
        changeStockPanel(event.getStockSymbol());
    }

    @Subscribe
    public void onChangeTheme(ChangeThemeEvent event) {
        changeTheme(event.getTheme());
    }

    @FXML
    private void onAddUser() {
        createView("user-creation-view", "UserCreationController",false);
    }

    @FXML
    private void onChangeUser() {
        createView("user-change-view", "UserChangeController", false);
    }

    @FXML
    private void onDeleteUser() {
         createView("user-delete-view", "UserDeleteController", false);
    }

    @FXML
    private void onEditUser() {
        createView("user-edit-view", "UserEditController", false);
    }

    @FXML
    private void onAddSpendingButtonClicked() {
        createView("spending-creation-view", "SpendingCreationController",false);
    }

    @FXML
    private void onBuyButtonClicked() {
        createView("stock-add-view", "StockAddController",false);
    }

    @FXML
    private void onSellButtonClicked() {
        createView("stock-sell-view", "StockSellController",false);
    }

    @FXML
    private void onAddProfitButtonClicked() {
        createView("profit-creation-view", "ProfitCreationController",false);
    }

    @FXML
    private void onDetailButtonClicked() {
        createView("expense-pie-chart-view", "ExpensePieChartController",true);
    }

    @FXML
    private void onPreferencesClicked() {
        System.out.println("Preferences");
        createView("preferences-change-view", "PreferencesChangeController",true);
    }

    private void updateMainScreen() {
        updateUserInfoPane();
        updateTransactionsInfoPane();
        updateUserStockInfoPane();
    }

    private void updateUserInfoPane() {
        int currentUserId = ServiceManager.loadLastUserId();
        if (currentUserId > 0) {
            User currentUser = UserDAO.getUser(currentUserId);
            helloLabel.setText(currentUser.getUsername() + ", witaj!");
            moneyLabel.setText(currentUser.getMoney() + "");
            double totalSpending = TransactionDAO.totalSpendingInMonth(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
            if (currentUser.getMonthLimit() > 0.0) {
                spendingLabel.setText("Wydatki w tym miesiącu: " + totalSpending
                        + "/" + currentUser.getMonthLimit() + "PLN");
                budgetIndicator.setSpendingValue(totalSpending, currentUser.getMonthLimit());
            }
            else
                spendingLabel.setText("Wydatki w tym miesiącu: " + totalSpending + "PLN");
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

    private void changeResolution(Dimension resolution) {
        primaryStage.setWidth(resolution.getWidth());
        primaryStage.setHeight(resolution.getHeight());
    }

    private void changeStockPanel(String newStockSymbol) {
        stockChart.setSymbol(newStockSymbol);
        stockChart.updateChart();
    }

    private void changeTheme(String newTheme) {
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();
        System.out.println(newTheme);
        String themePath = "/themes/" + newTheme;
        URL themeUrl = getClass().getResource(themePath);
        if (themeUrl == null) {
            System.err.println("Nie znaleziono pliku CSS: " + themePath);
            return;
        }
        scene.getStylesheets().clear();
        scene.getStylesheets().add(themeUrl.toExternalForm());
    }

    private void updateTransactionsInfoPane() {
        ArrayList<Transaction> expenses = TransactionDAO.getAllTransactions(1);
        VBox.setMargin(expensesVBox, new Insets(20, 0, 20, 0));
        expensesVBox.setSpacing(15);
        expensesVBox.getChildren().clear();

        if (expenses.isEmpty()) {
            Label label = new Label("Brak wydatków w ostatnim miesiącu :)");
            label.setStyle("-fx-font-size: 16px");
            expensesVBox.getChildren().add(label);
        } else {
            DecimalFormat currencyFormat = new DecimalFormat("#0.00");

            for (Transaction expense : expenses) {
                StringBuilder builder = new StringBuilder();

                // Pobierz kurs wymiany
                NbpApiClient client = new NbpApiClient(expense.getCurrencySymbol());
                double exchangeRate = client.getExchangeRate();

                // Przeliczona kwota w PLN
                double amountPLN = exchangeRate * expense.getPrice();

                // Zbuduj ładny opis
                builder.append(CategoryDAO.getById(expense.getCategoryId())).append(": ");
                builder.append(expense.getDescription());
                builder.append(" (").append(currencyFormat.format(amountPLN)).append(" PLN)");

                // Ustaw label i kolor
                Label label = new Label(builder.toString());
                if (Objects.equals(expense.getType(), "income")) {
                    label.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
                } else if (Objects.equals(expense.getType(), "expense")) {
                    label.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                }

                expensesVBox.getChildren().add(label);
            }
        }
    }


    private void updateUserStockInfoPane() {
        ArrayList<UserStock> stocks = UserStockDAO.getUserStocks(ServiceManager.loadLastUserId());
        VBox.setMargin(stocksVBox, new Insets(20, 0, 20, 0));
        stocksVBox.setSpacing(15);
        stocksVBox.getChildren().clear();
        if (stocks.isEmpty()) {
            Label label = new Label("Brak posiadanych akcji");
            label.setStyle("-fx-font-size: 16px");
            stocksVBox.getChildren().add(label);
        }
        else {
            for (UserStock stock : stocks) {
                StringBuilder builder = new StringBuilder();
                NbpApiClient client = new NbpApiClient(stock.getCurrency());
                double exchangeRate = client.getExchangeRate();

                Double purchasePrice = exchangeRate * stock.getPurchasePrice() * stock.getQuantity();
                DecimalFormat currencyFormat = new DecimalFormat("#0.00");
                builder.append(StockDAO.getStockSymbol(stock.getStockId())).append("(");
                builder.append(stock.getQuantity()).append(") zakup: ");
                builder.append(currencyFormat.format(purchasePrice)).append(" PLN, obecna: ");

                Double currentPrice = exchangeRate * stock.getQuantity() * StockPriceDAO.getLatestPrice(stock.getStockId());
                builder.append(currencyFormat.format(currentPrice)).append(" PLN");
                Label label = new Label(builder.toString());
                label.setStyle("-fx-font-size: 16px");
                stocksVBox.getChildren().add(label);
            }
        }
    }

    private void createView(String loaderView, String controllerClassName, boolean decorated) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/edp/projekt/" + loaderView + ".fxml"));

        //Reflection
        try {
        Class<?> clazz = Class.forName("com.edp.projekt.controller." + controllerClassName);
        Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
        loader.setController(controllerInstance);

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        if (!decorated)
            stage.initStyle(StageStyle.UNDECORATED);

        try {
            Method setStageMethod = clazz.getMethod("setStage", Stage.class);
            setStageMethod.invoke(controllerInstance, stage);
            Method setThemeMethod = clazz.getMethod("loadTheme");
            setThemeMethod.invoke(controllerInstance);
        } catch (NoSuchMethodException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.WARNING,
                    "Controller does not have setStage(Stage): " + controllerClassName);
        }

        stage.showAndWait();
        } catch (ClassNotFoundException | NoSuchMethodException | IOException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Error in createView: " + e.getMessage());
        }
    }
}

