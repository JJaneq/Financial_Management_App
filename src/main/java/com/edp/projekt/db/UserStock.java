package com.edp.projekt.db;

import com.edp.projekt.DAO.StockDAO;
import com.edp.projekt.DAO.StockPriceDAO;

import java.util.HashMap;
import java.util.Map;

public class UserStock {
    private int id;
    private int userId;
    private int stockId;
    private float purchasePrice;
    private int quantity;
    private String currency;
    private final float totalPrice;

    public UserStock(int id, int userId, int stockId, float purchasePrice, int quantity, String currency) {
        this.id = id;
        this.userId = userId;
        this.stockId = stockId;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.currency = currency;
        this.totalPrice = purchasePrice * quantity;
    }

    public UserStock(int userId, int stockId, float purchasePrice, int quantity, String currency) {
        this.id = 0;
        this.userId = userId;
        this.stockId = stockId;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.currency = currency;
        this.totalPrice = purchasePrice * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(float purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(StockDAO.getStockSymbol(this.stockId));
        stringBuilder.append("(").append(this.quantity);
        stringBuilder.append(") zakup: ").append(this.totalPrice);
        stringBuilder.append(", obecna: ").append(
                String.format("%.2f" ,StockPriceDAO.getLatestPrice(this.stockId) * this.quantity));
        stringBuilder.append(" ").append(this.currency);
        return stringBuilder.toString();
    }
}
