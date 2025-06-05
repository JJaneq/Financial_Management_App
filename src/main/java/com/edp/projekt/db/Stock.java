package com.edp.projekt.db;

import java.time.LocalDateTime;

public class Stock {
    private int id;
    private int open;
    private int close;
    private int volume;
    private int low;
    private int high;
    private String stockName;
    private String stockSymbol;
    private LocalDateTime stockTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
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
