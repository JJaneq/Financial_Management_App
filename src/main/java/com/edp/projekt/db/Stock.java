package com.edp.projekt.db;

import java.time.LocalDateTime;

public class Stock {
    private int id;
    private int price;
    private String stockName;
    private String stockSymbol;
    private LocalDateTime stockTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public LocalDateTime getStockTime() {
        return stockTime;
    }

    public void setStockTime(LocalDateTime stockTime) {
        this.stockTime = stockTime;
    }
}
