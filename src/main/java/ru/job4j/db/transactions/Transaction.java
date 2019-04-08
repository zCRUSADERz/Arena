package ru.job4j.db.transactions;

import ru.job4j.db.ConnectionHolder;

import java.sql.SQLException;

/**
 * Transaction.
 *
 * Close and rollback running as needed in the CloseConnection filter.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 07.04.2019
 */
public class Transaction {
    private final ConnectionHolder connectionHolder;

    public Transaction(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    /**
     * Start transaction.
     */
    public final void start() {
        try {
            this.connectionHolder.connection().setAutoCommit(false);
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Commit.
     */
    public final void commit() {
        try {
            this.connectionHolder.connection().commit();
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Complete the transaction.
     */
    public final void finish() {
        try {
            this.connectionHolder.connection().setAutoCommit(true);
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
