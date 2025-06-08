package com.edp.projekt.DAO;

import com.edp.projekt.db.DatabaseConnector;
import com.edp.projekt.db.Stock;
import com.edp.projekt.db.StockPrice;
import com.edp.projekt.external_api.FinancialApi;
import com.edp.projekt.service.AppExecutor;
import org.json.JSONObject;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockPriceDAO {
    private static final Logger logger = Logger.getLogger(StockDAO.class.getName());

    public static void addStockPrice(StockPrice stockPrice){
        String sql = "INSERT INTO stock_prices (stock_id, stock_time, open, close, high, low, volume) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, stockPrice.getStockId());
            ps.setTimestamp(2, Timestamp.valueOf(stockPrice.getStockTime()));
            ps.setDouble(3, stockPrice.getOpen());
            ps.setDouble(4, stockPrice.getClose());
            ps.setDouble(5, stockPrice.getHigh());
            ps.setDouble(6, stockPrice.getLow());
            ps.setDouble(7, stockPrice.getVolume());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in addStockPrice: " + e.getMessage());
        }
    }

    public static void addStockDataToNow(Stock stock) throws IOException, InterruptedException {
        String csvData = FinancialApi.getFinancialData(stock.getStockSymbol());
        ZoneId localZoneId = ZoneId.systemDefault(); // używamy strefy lokalnej
        List<StockPrice> prices = new ArrayList<>();

        // Rozdziel linie i pomiń nagłówek
        String[] lines = csvData.split("\\r?\\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split(",");

            if (parts.length != 6) continue; // Pomijanie błędnych linii

            try {
                LocalDate date = LocalDate.parse(parts[0], formatter);
                LocalDateTime localDateTime = date.atStartOfDay();

                double open = Double.parseDouble(parts[1]);
                double high = Double.parseDouble(parts[2]);
                double low = Double.parseDouble(parts[3]);
                double close = Double.parseDouble(parts[4]);
                long volume = (long) Double.parseDouble(parts[5]); // jeśli wolumen ma przecinki, konwersja na double i potem long

                StockPrice stockPrice = new StockPrice(
                        stock.getId(),
                        localDateTime,
                        open,
                        close,
                        high,
                        low,
                        volume
                );

                StockPriceDAO.addStockPrice(stockPrice);
            } catch (Exception e) {
                System.err.println("Błąd parsowania linii: " + lines[i]);
                e.printStackTrace();
            }
        }
        System.out.println("Skończono wgrywanie!");
    }


    private static LocalDateTime getLastStockPriceTime(int stockId) {
        String sql = "SELECT max(stock_time) AS last_time FROM stock_prices WHERE stock_id = ?";
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, stockId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp timestamp = rs.getTimestamp("last_time");
                if (timestamp != null) return timestamp.toLocalDateTime();
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getLastStockPriceTime: " + e.getMessage());
        }
        return null;
    }

    public static void updateStockPrices(){
        AppExecutor.getScheduler().scheduleAtFixedRate(() -> {
            ArrayList<Stock> stocks = StockDAO.getAllStocks();
            for (Stock stock : stocks) {
                System.out.println("Updating stock price for " + stock.getStockSymbol());
                try {
                    String stockData = FinancialApi.getFinancialData(stock.getStockSymbol());

                    String[] lines = stockData.split("\\r?\\n");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime lastTime = getLastStockPriceTime(stock.getId());
                    if (lastTime == null) {
                        StockPriceDAO.addStockDataToNow(stock);
                        continue;
                    }
                    for (int i = lines.length - 1; i >= 0 ; i--) {
                        String[] parts = lines[i].split(",");

                        if (parts.length != 6) continue; // Pomijanie błędnych linii

                        try {
                            LocalDate date = LocalDate.parse(parts[0], formatter);
                            LocalDateTime localDateTime = date.atStartOfDay();
                            if (localDateTime.isBefore(lastTime.plusDays(1))) {
                                System.out.println("breaking at " + localDateTime);
                                break;
                            }

                            double open = Double.parseDouble(parts[1]);
                            double high = Double.parseDouble(parts[2]);
                            double low = Double.parseDouble(parts[3]);
                            double close = Double.parseDouble(parts[4]);
                            long volume = (long) Double.parseDouble(parts[5]); // jeśli wolumen ma przecinki, konwersja na double i potem long

                            StockPrice stockPrice = new StockPrice(
                                    stock.getId(),
                                    localDateTime,
                                    open,
                                    close,
                                    high,
                                    low,
                                    volume
                            );

                            StockPriceDAO.addStockPrice(stockPrice);
                        } catch (Exception e) {
                            System.err.println("Błąd parsowania linii: " + lines[i]);
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Finished adding for " + stock.getStockSymbol());
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    public static ArrayList<StockPrice> getStockData(String stockSymbol) {
        String sql = "SELECT * FROM stock_prices WHERE stock_id = ?";
        ArrayList<StockPrice> stockPrices = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, StockDAO.getStock(stockSymbol).getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockPrice stockPrice = new StockPrice();
                stockPrice.setId(rs.getInt("id"));
                stockPrice.setStockId(rs.getInt("stock_id"));
                stockPrice.setOpen(rs.getDouble("open"));
                stockPrice.setHigh(rs.getDouble("high"));
                stockPrice.setLow(rs.getDouble("low"));
                stockPrice.setClose(rs.getDouble("close"));
                stockPrice.setVolume(rs.getLong("volume"));
                Timestamp stock_time = rs.getTimestamp("stock_time");
                stockPrice.setStockTime(stock_time.toLocalDateTime());
                stockPrices.add(stockPrice);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getStockData: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stockPrices;
    }

    public static ArrayList<StockPrice> getStockData(String stockSymbol, int days) {
        String sql = "SELECT * FROM stock_prices WHERE stock_id = ? " +
                "AND stock_time BETWEEN ? AND ?";
        ArrayList<StockPrice> stockPrices = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusDays(days);

        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, StockDAO.getStock(stockSymbol).getId());
            ps.setTimestamp(2, Timestamp.valueOf(begin.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(now.atStartOfDay()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StockPrice stockPrice = new StockPrice();
                stockPrice.setId(rs.getInt("id"));
                stockPrice.setStockId(rs.getInt("stock_id"));
                stockPrice.setOpen(rs.getDouble("open"));
                stockPrice.setHigh(rs.getDouble("high"));
                stockPrice.setLow(rs.getDouble("low"));
                stockPrice.setClose(rs.getDouble("close"));
                stockPrice.setVolume(rs.getLong("volume"));
                Timestamp stock_time = rs.getTimestamp("stock_time");
                stockPrice.setStockTime(stock_time.toLocalDateTime());
                stockPrices.add(stockPrice);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getStockData: " + e.getMessage());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stockPrices;
    }

    public static float getLatestPrice(int stockId) {
        String sql = "SELECT * FROM stock_prices WHERE stock_id = ? ORDER BY stock_time DESC LIMIT 1";
        float latestPrice = 0F;
        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, stockId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                latestPrice = Float.parseFloat(rs.getString("close"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error in getLatestPrice: " + e.getMessage());
        }
        return latestPrice;
    }
}
