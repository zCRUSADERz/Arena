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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * Connection wrapper.
 * Wraps all new Statement and PreparedStatement in special implementations
 * using Functions.
 *
 * @since 0.1
 */
@SuppressWarnings(
    {"PMD.TooManyMethods", "PMD.ExcessivePublicCount", "ClassWithTooManyMethods"})
public class ConnectionWrapper implements Connection {

    /**
     * Original connection.
     */
    private final Connection origin;

    /**
     * Wrapper for Statements.
     */
    private final Function<Statement, Statement> statements;

    /**
     * Wrapper for PreparedStatements.
     */
    private final Function<PreparedStatement, PreparedStatement> prepared;

    /**
     * Primary constructor.
     * @param origin Original connection.
     * @param statements Wrapper for Statements.
     * @param prepared Wrapper for PreparedStatements.
     */
    public ConnectionWrapper(final Connection origin,
        final Function<Statement, Statement> statements,
            final Function<PreparedStatement, PreparedStatement> prepared) {
        this.origin = origin;
        this.statements = statements;
        this.prepared = prepared;
    }

    @Override
    public final Statement createStatement() throws SQLException {
        final Statement statement = this.origin.createStatement();
        return this.statements.apply(statement);
    }

    @Override
    public final Statement createStatement(
        final int type, final int concurrency
    ) throws SQLException {
        final Statement statement =
            this.origin.createStatement(type, concurrency);
        return this.statements.apply(statement);
    }

    @Override
    public final Statement createStatement(
        final int type, final int concurrency, final int holdability
    ) throws SQLException {
        final Statement statement =
            this.origin.createStatement(type, concurrency, holdability);
        return this.statements.apply(statement);
    }

    @Override
    public final PreparedStatement prepareStatement(final String sql)
        throws SQLException {
        final PreparedStatement statement = this.origin.prepareStatement(sql);
        return this.prepared.apply(statement);
    }

    @Override
    public final PreparedStatement prepareStatement(
        final String sql, final int keys)
        throws SQLException {
        final PreparedStatement statement =
            this.origin.prepareStatement(sql, keys);
        return this.prepared.apply(statement);
    }

    @Override
    public final PreparedStatement prepareStatement(
        final String sql, final int[] columns
    ) throws SQLException {
        final PreparedStatement statement =
            this.origin.prepareStatement(sql, columns);
        return this.prepared.apply(statement);
    }

    @Override
    public final PreparedStatement prepareStatement(
        final String sql, final String[] columns
    ) throws SQLException {
        final PreparedStatement statement =
            this.origin.prepareStatement(sql, columns);
        return this.prepared.apply(statement);
    }

    @Override
    public final PreparedStatement prepareStatement(
        final String sql, final int type, final int concurrency
    ) throws SQLException {
        final PreparedStatement statement =
            this.origin.prepareStatement(sql, type, concurrency);
        return this.prepared.apply(statement);
    }

    // @checkstyle ParameterNumber (3 lines)
    @Override
    public final PreparedStatement prepareStatement(final String sql,
        final int type, final int concurrency, final int holdability
    ) throws SQLException {
        final PreparedStatement statement =
            this.origin.prepareStatement(sql, type, concurrency, holdability);
        return this.prepared.apply(statement);
    }

    @Override
    public final CallableStatement prepareCall(final String sql)
        throws SQLException {
        return this.origin.prepareCall(sql);
    }

    @Override
    public final String nativeSQL(final String sql) throws SQLException {
        return this.origin.nativeSQL(sql);
    }

    @Override
    public final void setAutoCommit(final boolean auto) throws SQLException {
        this.origin.setAutoCommit(auto);
    }

    @Override
    public final boolean getAutoCommit() throws SQLException {
        return this.origin.getAutoCommit();
    }

    @Override
    public final void commit() throws SQLException {
        this.origin.commit();
    }

    @Override
    public final void rollback() throws SQLException {
        this.origin.rollback();
    }

    @Override
    public final void close() throws SQLException {
        this.origin.close();
    }

    @Override
    public final boolean isClosed() throws SQLException {
        return this.origin.isClosed();
    }

    @Override
    public final DatabaseMetaData getMetaData() throws SQLException {
        return this.origin.getMetaData();
    }

    @Override
    public final void setReadOnly(final boolean only) throws SQLException {
        this.origin.setReadOnly(only);
    }

    @Override
    public final boolean isReadOnly() throws SQLException {
        return this.origin.isReadOnly();
    }

    @Override
    public final void setCatalog(final String catalog) throws SQLException {
        this.origin.setCatalog(catalog);
    }

    @Override
    public final String getCatalog() throws SQLException {
        return this.origin.getCatalog();
    }

    @Override
    public final void setTransactionIsolation(final int level)
        throws SQLException {
        this.origin.setTransactionIsolation(level);
    }

    @Override
    public final int getTransactionIsolation() throws SQLException {
        return this.origin.getTransactionIsolation();
    }

    @Override
    public final SQLWarning getWarnings() throws SQLException {
        return this.origin.getWarnings();
    }

    @Override
    public final void clearWarnings() throws SQLException {
        this.origin.clearWarnings();
    }

    @Override
    public final CallableStatement prepareCall(
        final String sql, final int type, final int concurrency
    ) throws SQLException {
        return this.origin.prepareCall(sql, type, concurrency);
    }

    @Override
    public final Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.origin.getTypeMap();
    }

    @Override
    public final void setTypeMap(final Map<String, Class<?>> map)
        throws SQLException {
        this.origin.setTypeMap(map);
    }

    @Override
    public final void setHoldability(final int holdability)
        throws SQLException {
        this.origin.setHoldability(holdability);
    }

    @Override
    public final int getHoldability() throws SQLException {
        return this.origin.getHoldability();
    }

    @Override
    public final Savepoint setSavepoint() throws SQLException {
        return this.origin.setSavepoint();
    }

    @Override
    public final Savepoint setSavepoint(final String name) throws SQLException {
        return this.origin.setSavepoint(name);
    }

    @Override
    public final void rollback(final Savepoint savepoint) throws SQLException {
        this.origin.rollback(savepoint);
    }

    @Override
    public final void releaseSavepoint(final Savepoint savepoint)
        throws SQLException {
        this.origin.releaseSavepoint(savepoint);
    }

    // @checkstyle ParameterNumber (3 lines)
    @Override
    public final CallableStatement prepareCall(final String sql, final int type,
        final int concurrency, final int holdability
    ) throws SQLException {
        return this.origin.prepareCall(
            sql, type, concurrency, holdability
        );
    }

    @Override
    public final Clob createClob() throws SQLException {
        return this.origin.createClob();
    }

    @Override
    public final Blob createBlob() throws SQLException {
        return this.origin.createBlob();
    }

    @Override
    public final NClob createNClob() throws SQLException {
        return this.origin.createNClob();
    }

    @Override
    public final SQLXML createSQLXML() throws SQLException {
        return this.origin.createSQLXML();
    }

    @Override
    public final boolean isValid(final int timeout) throws SQLException {
        return this.origin.isValid(timeout);
    }

    @Override
    public final void setClientInfo(final String name, final String value)
        throws SQLClientInfoException {
        this.origin.setClientInfo(name, value);
    }

    @Override
    public final void setClientInfo(final Properties properties)
        throws SQLClientInfoException {
        this.origin.setClientInfo(properties);
    }

    @Override
    public final String getClientInfo(final String name) throws SQLException {
        return this.origin.getClientInfo(name);
    }

    @Override
    public final Properties getClientInfo() throws SQLException {
        return this.origin.getClientInfo();
    }

    @Override
    public final Array createArrayOf(final String type, final Object[] elements)
        throws SQLException {
        return this.origin.createArrayOf(type, elements);
    }

    @Override
    public final Struct createStruct(
        final String type, final Object[] attributes
    ) throws SQLException {
        return this.origin.createStruct(type, attributes);
    }

    @Override
    public final void setSchema(final String schema) throws SQLException {
        this.origin.setSchema(schema);
    }

    @Override
    public final String getSchema() throws SQLException {
        return this.origin.getSchema();
    }

    @Override
    public final void abort(final Executor executor) throws SQLException {
        this.origin.abort(executor);
    }

    @Override
    public final void setNetworkTimeout(final Executor executor,
        final int milliseconds) throws SQLException {
        this.origin.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public final int getNetworkTimeout() throws SQLException {
        return this.origin.getNetworkTimeout();
    }

    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        return this.origin.unwrap(iface);
    }

    @Override
    public final boolean isWrapperFor(final Class<?> iface)
        throws SQLException {
        return this.origin.isWrapperFor(iface);
    }
}
