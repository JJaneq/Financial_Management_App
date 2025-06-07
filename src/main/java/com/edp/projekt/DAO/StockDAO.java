package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.Stock;
import com.edp.projekt.external_api.FinancialApi;
import com.edp.projekt.service.AppExecutor;
import javafx.scene.chart.PieChart;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockDAO {
    private static final Logger logger = Logger.getLogger(StockDAO.class.getName());

    public static boolean addStock(String symbol) throws IOException, InterruptedException {
        if (!FinancialApi.isStockExist(symbol))
            return false;

        String sql = "INSERT INTO stocks (stock_symbol) VALUES (?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, symbol);
            ps.executeUpdate();
            AppExecutor.getExecutor().submit(() -> {
                try {
                    System.out.println("THREAD");
                    StockPriceDAO.addStockDataToNow(getStock(symbol));
                } catch (IOException | InterruptedException e) {
                    logger.log(Level.SEVERE, "Error loading stock price data", e);
                }
            });
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in addStock" + e.getMessage());
            return false;
        }
    }

    public static Stock getStock(String symbol) throws IOException, InterruptedException {
        String sql = "SELECT * FROM stocks WHERE stock_symbol = ?";
        Stock stock = null;
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, symbol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stock = new Stock(rs.getString("stock_symbol"));
                stock.setId(rs.getInt("id"));
            }
        } catch(SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getStock" + e.getMessage());
        }

//        if (stock != null)
//            StockPriceDAO.addStockDataToNow(stock);
        return stock;
    }

    public static ArrayList<Stock> getAllStocks() {
        ArrayList<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stocks";
        try(Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Stock stock = new Stock(rs.getString("stock_symbol"));
                stock.setId(rs.getInt("id"));
                stocks.add(stock);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getAllStocks" + e.getMessage());
        }
        return stocks;
    }
}
