package com.edp.projekt.db;

import com.edp.projekt.DAO.UserDAO;

public class User {
    private int id;
    private String username;
    private float money;
    private float monthLimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(float monthLimit) {
        this.monthLimit = monthLimit;
    }

    @Override
    public String toString() {
        return username;
    }

    public void handleTransaction(Transaction transaction) {
        if (transaction.getType().equals("expense"))
            this.money -= transaction.getPrice();
        else if (transaction.getType().equals("income"))
            this.money += transaction.getPrice();
        UserDAO.updateUser(this.id, this);
    }
}
