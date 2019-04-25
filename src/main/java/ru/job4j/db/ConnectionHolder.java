/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Yakovlev Alexander
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ru.job4j.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Thread local connection holder.
 *
 * @since 0.1
 */
@SuppressWarnings("ThreadLocalNotStaticFinal")
public class ConnectionHolder implements AutoCloseable {

    /**
     * Data source.
     */
    private final DataSource source;

    /**
     * Connection holder.
     */
    private final ThreadLocal<Connection> holder;

    /**
     * A boolean flag indicating whether the thread is currently
     * holding the connection or not.
     */
    private final ThreadLocal<Boolean> holds;

    /**
     * Secondary constructor.
     * @param source Data source.
     */
    public ConnectionHolder(final DataSource source) {
        this(
            source,
            new ThreadLocal<>(),
            ThreadLocal.withInitial(() -> false)
        );
    }

    /**
     * Primary constructor.
     * @param source Data source.
     * @param holder Connection holder.
     * @param holds A boolean flag indicating whether the thread is currently
     *  holding the connection or not.
     */
    public ConnectionHolder(final DataSource source,
        final ThreadLocal<Connection> holder, final ThreadLocal<Boolean> holds
    ) {
        this.source = source;
        this.holder = holder;
        this.holds = holds;
    }

    /**
     * Return connection. If the connection was not held, a new connection
     * will be created using DataSource.
     * @return Connection.
     */
    public final Connection connection() {
        final Connection connection;
        if (this.holds.get()) {
            connection = this.holder.get();
        } else {
            try {
                connection = this.source.getConnection();
                this.holder.set(connection);
                this.holds.set(true);
            } catch (final SQLException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return connection;
    }

    /**
     * Closes connection if it is held.
     * @throws SQLException If Connection.close() throw SQLException.
     */
    public final void close() throws SQLException {
        if (this.holds.get()) {
            try {
                this.holder.get().close();
            } finally {
                this.holds.set(false);
            }
        }
    }

    /**
     * Commit, if connection is held.
     * @throws SQLException If Connection.commit() throw SQLException.
     */
    public final void commit() throws SQLException {
        if (this.holds.get()) {
            if (!this.holder.get().getAutoCommit()) {
                this.holder.get().commit();
            }
        }
    }

    /**
     * Rollback, if connection is held.
     * @throws SQLException If Connection.rollback() throw SQLException.
     */
    public final void rollback() throws SQLException {
        if (this.holds.get()) {
            this.holder.get().rollback();
        }
    }
}
