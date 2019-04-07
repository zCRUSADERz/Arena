package ru.job4j.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder implements AutoCloseable {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> connectionHolder;
    private final ThreadLocal<Boolean> holdsNow;

    public ConnectionHolder(final DataSource dataSource,
                            final ThreadLocal<Connection> connectionHolder,
                            final ThreadLocal<Boolean> holdsNow) {
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
        this.holdsNow = holdsNow;
    }

    public final Connection connection() {
        final Connection connection;
        if (this.holdsNow.get()) {
            connection = this.connectionHolder.get();
        } else {
            try {
                connection = this.dataSource.getConnection();
                this.connectionHolder.set(connection);
                this.holdsNow.set(true);
            } catch (final SQLException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return connection;
    }

    public final void close() throws Exception {
        if (this.holdsNow.get()) {
            try {
                this.connectionHolder.get().close();
            } finally {
                this.holdsNow.set(false);
            }
        }
    }

    public final void commit() throws SQLException {
        if (this.holdsNow.get()) {
            this.connectionHolder.get().commit();
        }
    }

    public final void rollback() throws Exception {
        if (this.holdsNow.get()) {
            this.connectionHolder.get().rollback();
        }
    }
}
