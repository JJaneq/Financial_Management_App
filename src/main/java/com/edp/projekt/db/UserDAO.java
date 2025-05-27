package com.edp.projekt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public static void addUser(User user) {
        String sql = "INSERT INTO users (username, money, monthly_limit) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnector.connect();) {
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

    public static User getUser(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setMoney(rs.getFloat("money"));
                user.setMonthLimit(rs.getFloat("monthly_limit"));
                return user;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot load user from database: " + e.getMessage(), e);
        }
        return null;
    }

    public static User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnector.connect();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setMoney(rs.getFloat("money"));
                user.setMonthLimit(rs.getFloat("monthly_limit"));
                return user;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot load user from database: " + e.getMessage(), e);
        }
        return null;
    }
}
