module com.edp.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires mysql.connector.j;


    opens com.edp.projekt to javafx.fxml;
    exports com.edp.projekt.app;
    exports com.edp.projekt.db;
    exports com.edp.projekt.service;
    opens com.edp.projekt.app to javafx.fxml;
    exports com.edp.projekt.controller;
    opens com.edp.projekt.controller to javafx.fxml;
}