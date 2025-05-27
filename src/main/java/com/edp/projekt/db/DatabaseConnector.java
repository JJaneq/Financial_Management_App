package com.edp.projekt.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./src/")
            .ignoreIfMissing()
            .ignoreIfMalformed()
            .load();
    protected static String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USERNAME");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    private static Connection connection;

    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

//    public static Connection getConnection() throws SQLException, ClassNotFoundException {
//        if (connection == null) {
//            connection = connect();
//        }
//        return connection;
//    }
}
