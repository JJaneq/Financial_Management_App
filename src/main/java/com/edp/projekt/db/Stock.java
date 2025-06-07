package com.edp.projekt.db;

import java.time.LocalDateTime;

public class Stock {
    private int id;
    private String stockSymbol;

    public Stock(String stockSymbol) {
//        this.id = StockDAO.getStockId;
        this.stockSymbol = stockSymbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
}
