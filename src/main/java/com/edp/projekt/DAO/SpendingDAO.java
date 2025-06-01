package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.Expense;
import com.edp.projekt.service.ServiceManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpendingDAO {
    private static final Logger logger = Logger.getLogger(SpendingDAO.class.getName());

    public static ArrayList<Expense> getSpendings(){
        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Expense> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ServiceManager.loadLastUserId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                Timestamp sqlTimestamp = rs.getTimestamp("created_at");
                if(sqlTimestamp != null){
                    expense.setExpenseTime(sqlTimestamp.toLocalDateTime());
                }
                else {
                    expense.setExpenseTime(LocalDateTime.now());
                }
                expenses.add(expense);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAllSpending: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<Expense> getSpendings(int months){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMonths(months);
        String sql = "SELECT * FROM expenses WHERE expense_time BETWEEN ? AND ? AND user_id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Expense> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(startTime));
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setInt(3, ServiceManager.loadLastUserId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                expense.setUserId(rs.getInt("user_id"));
                Timestamp sqlTimestamp = rs.getTimestamp("expense_time");
                if(sqlTimestamp != null){
                    expense.setExpenseTime(sqlTimestamp.toLocalDateTime());
                }
                else {
                    expense.setExpenseTime(LocalDateTime.now());
                }
                expenses.add(expense);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAllSpending: " + e.getMessage(), e);
        }
        return null;
    }

    public static void addSpending(Expense expense){
        String sql = "INSERT INTO expenses " +
                "(user_id, category_id, description, price, currency_symbol, expense_time)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ServiceManager.loadLastUserId());
            ps.setInt(2, expense.getCategoryId());
            ps.setString(3, expense.getDescription());
            ps.setFloat(4, expense.getPrice());
            ps.setString(5, expense.getCurrencySymbol());
            ps.setTimestamp(6, Timestamp.valueOf(expense.getExpenseTime()));
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in addSpending: " + e.getMessage(), e);
        }
    }
}
