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
package ru.job4j.db.wrappers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Statement wrapper.
 * Wrap original Statement and MeasuringStatement.
 *
 * @since 0.1
 */
@SuppressWarnings(
    {"PMD.TooManyMethods", "PMD.ExcessivePublicCount", "ClassWithTooManyMethods"})
public class StatementWrapper extends MeasuringStatement implements Statement {

    /**
     * Original Statement.
     */
    private final Statement original;

    /**
     * Secondary constructor.
     * @param original Original Statement.
     * @param counter Query counter.
     * @param timer Query timer.
     */
    public StatementWrapper(final Statement original,
        final ThreadLocal<Integer> counter, final ThreadLocal<Long> timer
    ) {
        this(original, new QueryMeter(counter, timer));
    }

    /**
     * Primary constructor.
     * @param original Original Statement.
     * @param meter Query meter.
     */
    public StatementWrapper(
        final Statement original, final QueryMeter meter
    ) {
        super(original, meter);
        this.original = original;
    }

    @Override
    public final void close() throws SQLException {
        this.original.close();
    }

    @Override
    public final int getMaxFieldSize() throws SQLException {
        return this.original.getMaxFieldSize();
    }

    @Override
    public final void setMaxFieldSize(final int max) throws SQLException {
        this.original.setMaxFieldSize(max);
    }

    @Override
    public final int getMaxRows() throws SQLException {
        return this.original.getMaxRows();
    }

    @Override
    public final void setMaxRows(final int max) throws SQLException {
        this.original.setMaxRows(max);
    }

    @Override
    public final void setEscapeProcessing(final boolean enable)
        throws SQLException {
        this.original.setEscapeProcessing(enable);
    }

    @Override
    public final int getQueryTimeout() throws SQLException {
        return this.original.getQueryTimeout();
    }

    @Override
    public final void setQueryTimeout(final int seconds) throws SQLException {
        this.original.setQueryTimeout(seconds);
    }

    @Override
    public final void cancel() throws SQLException {
        this.original.cancel();
    }

    @Override
    public final SQLWarning getWarnings() throws SQLException {
        return this.original.getWarnings();
    }

    @Override
    public final void clearWarnings() throws SQLException {
        this.original.clearWarnings();
    }

    @Override
    public final void setCursorName(final String name) throws SQLException {
        this.original.setCursorName(name);
    }

    @Override
    public final ResultSet getResultSet() throws SQLException {
        return this.original.getResultSet();
    }

    @Override
    public final int getUpdateCount() throws SQLException {
        return this.original.getUpdateCount();
    }

    @Override
    public final boolean getMoreResults() throws SQLException {
        return this.original.getMoreResults();
    }

    @Override
    public final void setFetchDirection(final int direction)
        throws SQLException {
        this.original.setFetchDirection(direction);
    }

    @Override
    public final int getFetchDirection() throws SQLException {
        return this.original.getFetchDirection();
    }

    @Override
    public final void setFetchSize(final int rows) throws SQLException {
        this.original.setFetchSize(rows);
    }

    @Override
    public final int getFetchSize() throws SQLException {
        return this.original.getFetchSize();
    }

    @Override
    public final int getResultSetConcurrency() throws SQLException {
        return this.original.getResultSetConcurrency();
    }

    @Override
    public final int getResultSetType() throws SQLException {
        return this.original.getResultSetType();
    }

    @Override
    public final void addBatch(final String sql) throws SQLException {
        this.original.addBatch(sql);
    }

    @Override
    public final void clearBatch() throws SQLException {
        this.original.clearBatch();
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return this.original.getConnection();
    }

    @Override
    public final boolean getMoreResults(final int current) throws SQLException {
        return this.original.getMoreResults(current);
    }

    @Override
    public final ResultSet getGeneratedKeys() throws SQLException {
        return this.original.getGeneratedKeys();
    }

    @Override
    public final int getResultSetHoldability() throws SQLException {
        return this.original.getResultSetHoldability();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return this.original.isClosed();
    }

    @Override
    public final void setPoolable(final boolean poolable) throws SQLException {
        this.original.setPoolable(poolable);
    }

    @Override
    public final boolean isPoolable() throws SQLException {
        return this.original.isPoolable();
    }

    @Override
    public final void closeOnCompletion() throws SQLException {
        this.original.closeOnCompletion();
    }

    @Override
    public final boolean isCloseOnCompletion() throws SQLException {
        return this.original.isCloseOnCompletion();
    }

    @Override
    public final long getLargeUpdateCount() throws SQLException {
        return this.original.getLargeUpdateCount();
    }

    @Override
    public final void setLargeMaxRows(final long max) throws SQLException {
        this.original.setLargeMaxRows(max);
    }

    @Override
    public final long getLargeMaxRows() throws SQLException {
        return this.original.getLargeMaxRows();
    }

    @Override
    public final String enquoteLiteral(final String val) throws SQLException {
        return this.original.enquoteLiteral(val);
    }

    @Override
    public final String enquoteIdentifier(
        final String identifier, final boolean quote
    ) throws SQLException {
        return this.original.enquoteIdentifier(identifier, quote);
    }

    @Override
    public final boolean isSimpleIdentifier(final String identifier)
        throws SQLException {
        return this.original.isSimpleIdentifier(identifier);
    }

    @Override
    public final String enquoteNCharLiteral(final String val)
        throws SQLException {
        return this.original.enquoteNCharLiteral(val);
    }

    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.original.unwrap(iface);
    }

    @Override
    public final boolean isWrapperFor(final Class<?> iface)
        throws SQLException {
        return this.original.isWrapperFor(iface);
    }
}
