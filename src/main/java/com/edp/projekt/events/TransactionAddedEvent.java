package com.edp.projekt.events;

import com.edp.projekt.db.Transaction;

public class TransactionAddedEvent {
    private final Transaction transaction;

    public TransactionAddedEvent(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}

