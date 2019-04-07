package ru.job4j.db.transactions;

import ru.job4j.db.ConnectionHolder;

import java.sql.SQLException;

public class Transaction {
    private final ConnectionHolder connectionHolder;

    public Transaction(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public final void start() {
        try {
            this.connectionHolder.connection().setAutoCommit(false);
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public final void commit() {
        try {
            this.connectionHolder.connection().commit();
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
