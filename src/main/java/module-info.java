module com.edp.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires mysql.connector.j;
    requires java.net.http;
    requires org.json;
    requires java.desktop;
    requires com.google.common;
    requires org.apache.commons.lang3;


    opens com.edp.projekt to javafx.fxml;
    opens com.edp.projekt.components to javafx.fxml;
    exports com.edp.projekt.app;
    exports com.edp.projekt.db;
    exports com.edp.projekt.service;
    opens com.edp.projekt.app to javafx.fxml;
    exports com.edp.projekt.controller;
    opens com.edp.projekt.controller to javafx.fxml;
    exports com.edp.projekt.DAO;
}