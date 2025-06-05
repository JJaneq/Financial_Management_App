package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.User;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public static void addUser(User user) {
        String sql = "INSERT INTO users (username, money, month_limit) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnector.connect();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setFloat(2, user.getMoney());
            ps.setFloat(3, user.getMonthLimit());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot add user to database: " + e.getMessage(), e);
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
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setMoney(rs.getFloat("money"));
                user.setMonthLimit(rs.getFloat("month_limit"));
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
                user.setMonthLimit(rs.getFloat("month_limit"));
                return user;
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot load user from database: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setMoney(rs.getFloat("money"));
                user.setMonthLimit(rs.getFloat("month_limit"));
                users.add(user);
            }
            return users;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot load users from database: " + e.getMessage(), e);
        }
        return null;
    }

    public static void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot delete user from database: " + e.getMessage(), e);
        }
    }

    public static void updateUser(int id, User newUser) {
        String sql = "UPDATE users SET username = ?, money = ?, month_limit = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newUser.getUsername());
            ps.setFloat(2, newUser.getMoney());
            ps.setFloat(3, newUser.getMonthLimit());
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Cannot update user from database: " + e.getMessage(), e);
        }
    }

    public static void changeUser(User user) {
        String sql = "UPDATE users SET money = ?, month_limit = ? WHERE id = ?";
        System.out.println("User:" + user.getId());
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setFloat(1, user.getMoney());
            ps.setFloat(2, user.getMonthLimit());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
            ResultSet rs = ps.executeQuery("SELECT money FROM users WHERE id = 0");
            System.out.println(rs.getFloat("money"));
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in changeUser: " + e.getMessage(), e);
        }
    }
}
