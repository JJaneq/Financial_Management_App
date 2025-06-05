package com.edp.projekt.db;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int userId;
    private int categoryId;
    private float price;
    private LocalDateTime expenseTime;
    private String description;
    private String currencySymbol;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getExpenseTime() {
        return expenseTime;
    }

    public void setExpenseTime(LocalDateTime expenseTime) {
        this.expenseTime = expenseTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return price + " " + currencySymbol + ": " + description + " - " + expenseTime.toLocalDate().toString();
    }
}
