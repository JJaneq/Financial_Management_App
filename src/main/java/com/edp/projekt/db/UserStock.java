package com.edp.projekt.db;

public class UserStock {
    private int id;
    private int userId;
    private int stockId;
    private float purchasePrice;
    private int quantity;

    public UserStock(int userId, int stockId, float purchasePrice, int quantity) {
        this.id = 0;
        this.userId = userId;
        this.stockId = stockId;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
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
}
