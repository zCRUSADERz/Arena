package ru.job4j.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Thread local connection holder.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 5.04.2019
 */
public class ConnectionHolder implements AutoCloseable {
    private final DataSource dataSource;
    private final ThreadLocal<Connection> connectionHolder;
    /**
     * A boolean flag indicating whether the thread is currently
     * holding the connection or not.
     */
    private final ThreadLocal<Boolean> holdsNow;

    public ConnectionHolder(final DataSource dataSource) {
        this(
                dataSource,
                new ThreadLocal<>(),
                ThreadLocal.withInitial(() -> false)
        );
    }

    public ConnectionHolder(final DataSource dataSource,
                            final ThreadLocal<Connection> connectionHolder,
                            final ThreadLocal<Boolean> holdsNow) {
        this.dataSource = dataSource;
        this.connectionHolder = connectionHolder;
        this.holdsNow = holdsNow;
    }

    /**
     * Return connection. If the connection was not held, a new connection
     * will be created using DataSource.
     * @return connection.
     */
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

    /**
     * Closes connection if it is held.
     * @throws Exception exception.
     */
    public final void close() throws Exception {
        if (this.holdsNow.get()) {
            try {
                this.connectionHolder.get().close();
            } finally {
                this.holdsNow.set(false);
            }
        }
    }

    /**
     * Commit, if connection is held.
     * @throws SQLException exception.
     */
    public final void commit() throws SQLException {
        if (this.holdsNow.get()) {
            if (!this.connectionHolder.get().getAutoCommit()) {
                this.connectionHolder.get().commit();
            }
        }
    }

    /**
     * Rollback, if connection is held.
     * @throws Exception exception.
     */
    public final void rollback() throws Exception {
        if (this.holdsNow.get()) {
            this.connectionHolder.get().rollback();
        }
    }
}
