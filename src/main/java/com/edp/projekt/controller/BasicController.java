package com.edp.projekt.controller;

import com.edp.projekt.events.ChangeThemeEvent;
import com.edp.projekt.service.EventBusManager;
import com.edp.projekt.service.ServiceManager;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public abstract class BasicController {
    Stage primaryStage;

    @FXML
    public void initialize(){
        EventBusManager.register(this);
    }

    @Subscribe
    public void onChangeTheme(ChangeThemeEvent event){
        changeTheme(event.getTheme());
    }

    public void loadTheme(){
        String theme = ServiceManager.getPreferredTheme();
        changeTheme(theme);
    }

    protected void changeTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        String css = Objects.requireNonNull(BasicController.class.getResource("/themes/" + theme + ".css")).toExternalForm();
        scene.getStylesheets().add(css);
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    protected void changeTheme(String newTheme) {
        Scene scene = primaryStage.getScene();

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
}
