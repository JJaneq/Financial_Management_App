package com.edp.projekt.db;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StockPrice {
    private int id;
    private int stockId;
    private LocalDateTime stockTime;
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;

    public StockPrice() {}

    public StockPrice(int stockId, LocalDateTime stockTime, double open, double close, double high, double low, double volume) {
        this.stockId = stockId;
        this.stockTime = stockTime;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public LocalDateTime getStockTime() {
        return stockTime;
    }

    public void setStockTime(LocalDateTime stockTime) {
        this.stockTime = stockTime;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
