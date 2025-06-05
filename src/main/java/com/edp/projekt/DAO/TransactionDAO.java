package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.Transaction;
import com.edp.projekt.service.ServiceManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionDAO {
    private static final Logger logger = Logger.getLogger(TransactionDAO.class.getName());

    public static ArrayList<Transaction> getSpendings(){
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND type = 'expense' ORDER BY expense_time DESC";
        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Transaction> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ServiceManager.loadLastUserId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Transaction expense = new Transaction();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                Timestamp sqlTimestamp = rs.getTimestamp("created_at");
                expense.setType(rs.getString("type"));
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

    public static ArrayList<Transaction> getSpendings(int months){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMonths(months);
        String sql = "SELECT * FROM transactions WHERE expense_time BETWEEN ? AND ? AND user_id = ? AND type = 'expense' ORDER BY expense_time DESC";
        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Transaction> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(startTime));
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setInt(3, ServiceManager.loadLastUserId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Transaction expense = new Transaction();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                expense.setUserId(rs.getInt("user_id"));
                Timestamp sqlTimestamp = rs.getTimestamp("expense_time");
                expense.setType(rs.getString("type"));
                if(sqlTimestamp != null){
                    expense.setExpenseTime(sqlTimestamp.toLocalDateTime());
                }
                else {
                    expense.setExpenseTime(LocalDateTime.now());
                }
                expenses.add(expense);
            }
            return expenses;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAllSpending: " + e.getMessage(), e);
        }
        return null;
    }

    public static ArrayList<Transaction> getAllTransactions(int months){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMonths(months);
        String sql = "SELECT * FROM transactions WHERE expense_time BETWEEN ? AND ? AND user_id = ? ORDER BY expense_time DESC";
        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Transaction> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(startTime));
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setInt(3, ServiceManager.loadLastUserId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Transaction expense = new Transaction();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                expense.setUserId(rs.getInt("user_id"));
                Timestamp sqlTimestamp = rs.getTimestamp("expense_time");
                expense.setType(rs.getString("type"));
                if(sqlTimestamp != null){
                    expense.setExpenseTime(sqlTimestamp.toLocalDateTime());
                }
                else {
                    expense.setExpenseTime(LocalDateTime.now());
                }
                expenses.add(expense);
            }
            return expenses;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAllSpending: " + e.getMessage(), e);
        }
        return null;
    }

    public static void getSpendingFromMonth(int month, int year){
        String sql = "SELECT * FROM transactions WHERE expense_time BETWEEN ? AND ? AND type = 'expense'";
        LocalDate startTime = LocalDate.of(year, month, 1);
        LocalDate endTime = startTime.withDayOfMonth(startTime.lengthOfMonth());
        Timestamp startTimestamp = Timestamp.valueOf(startTime.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endTime.atTime(23, 59, 59));

        try (Connection conn = DatabaseConnector.connect()) {
            ArrayList<Transaction> expenses = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, startTimestamp);
            ps.setTimestamp(2, endTimestamp);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Transaction expense = new Transaction();
                expense.setId(rs.getInt("id"));
                expense.setPrice(rs.getFloat("price"));
                expense.setCategoryId(rs.getInt("category_id"));
                expense.setDescription(rs.getString("description"));
                expense.setCurrencySymbol(rs.getString("currency_symbol"));
                expense.setUserId(rs.getInt("user_id"));
                Timestamp sqlTimestamp = rs.getTimestamp("expense_time");
                expense.setType(rs.getString("type"));
                if(sqlTimestamp != null){
                    expense.setExpenseTime(sqlTimestamp.toLocalDateTime());
                }
                else {
                    expense.setExpenseTime(LocalDateTime.now());
                }
                expenses.add(expense);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getSpendingFromMonth: " + e.getMessage(), e);
        }
    }

    public static void addTransaction(Transaction transaction){
        String sql = "INSERT INTO transactions " +
                "(user_id, category_id, description, price, currency_symbol, expense_time, type)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ServiceManager.loadLastUserId());
            ps.setInt(2, transaction.getCategoryId());
            ps.setString(3, transaction.getDescription());
            ps.setFloat(4, transaction.getPrice());
            ps.setString(5, transaction.getCurrencySymbol());
            ps.setTimestamp(6, Timestamp.valueOf(transaction.getExpenseTime()));
            ps.setString(7, transaction.getType());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in addSpending: " + e.getMessage(), e);
        }
    }

//    public static float totalSpendingInMonth(int month, int year){
//
//    }
}
