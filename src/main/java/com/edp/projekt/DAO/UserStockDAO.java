package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.UserStock;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserStockDAO {
    private static final Logger logger = Logger.getLogger(UserStockDAO.class.getName());

    public static void addUserStock(UserStock userStock){
        String sql = "INSERT INTO user_stocks (user_id, stock_id, purchase_price, quantity, currency) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userStock.getUserId());
            ps.setInt(2, userStock.getStockId());
            ps.setDouble(3, userStock.getPurchasePrice());
            ps.setInt(4, userStock.getQuantity());
            ps.setString(5, userStock.getCurrency());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in addUserStock: " + e.getMessage());
        }
    }

    public static void editUserStock(UserStock userStock) {
        String sql = "UPDATE user_stocks SET purchase_price = ?, quantity = ?, currency = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, userStock.getPurchasePrice());
            ps.setInt(2, userStock.getQuantity());
            ps.setString(3, userStock.getCurrency());
            ps.setInt(4, userStock.getId());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User stock updated successfully.");
            } else {
                System.out.println("No matching record found to update.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in editUserStock: " + e.getMessage());
        }
    }

    public static void deleteUserStock(UserStock userStock) {
        String sql = "DELETE FROM user_stocks WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userStock.getId());

            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("User stock deleted successfully.");
            } else {
                System.out.println("No matching record found to delete.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in deleteUserStock: " + e.getMessage());
        }
    }


    public static ArrayList<UserStock> getUserStocks(int userId){
        ArrayList<UserStock> userStocks = new ArrayList<>();
        String sql = "SELECT * FROM user_stocks WHERE user_id=?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserStock stock = new UserStock(userId, rs.getInt("stock_id"), rs.getInt("purchase_price"), rs.getInt("quantity"), rs.getString("currency"));
                stock.setId(rs.getInt("id"));
                userStocks.add(stock);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getUserStocks: " + e.getMessage());
        }
        return userStocks;
    }

    public static UserStock getUserStockById(int id){
        String sql = "SELECT * FROM user_stocks WHERE id=?";
        UserStock userStock = null;
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userStock = new UserStock(rs.getInt("id"), rs.getInt("user_id"),
                        rs.getInt("stock_id"), rs.getInt("purchase_price"),
                        rs.getInt("quantity"), rs.getString("currency"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getUserStockById: " + e.getMessage());
        }
        return userStock;
    }
}
