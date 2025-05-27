package com.edp.projekt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private DatabaseConnector databaseConnector;

    public UserDAO(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public static void addUser(User user) {
        String sql = "INSERT INTO users (username, money, monthly_limit) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setFloat(2, user.getMoney());
            ps.setFloat(3, user.getMonthLimit());
            ps.executeUpdate();

            System.out.println("User " + user.getUsername() + " added");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
