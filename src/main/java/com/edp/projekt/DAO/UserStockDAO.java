package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.UserStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
