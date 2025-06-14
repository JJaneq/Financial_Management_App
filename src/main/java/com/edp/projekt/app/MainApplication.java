package com.edp.projekt.app;

import com.edp.projekt.DAO.StockDAO;
import com.edp.projekt.controller.MainController;
import com.edp.projekt.service.AppExecutor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/edp/projekt/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        MainController mainController = fxmlLoader.getController();
        stage.setTitle("Financial App");
        stage.setScene(scene);
        mainController.setStage(stage);
        stage.show();
    }

    public void stop() throws Exception {
        super.stop();
        AppExecutor.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}