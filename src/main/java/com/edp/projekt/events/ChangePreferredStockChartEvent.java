package com.edp.projekt.events;

import com.edp.projekt.db.Stock;

public class ChangePreferredStockChartEvent {
    private final String stockSymbol;

    public ChangePreferredStockChartEvent(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }
}
