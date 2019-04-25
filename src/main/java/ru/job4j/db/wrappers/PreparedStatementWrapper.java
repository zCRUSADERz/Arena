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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

// @checkstyle MethodCount (10 lines)

/**
 * PreparedStatement wrapper.
 * Wrap original PreparedStatement and MeasuringPreparedStatement.
 *
 * @since 0.1
 */
@SuppressWarnings({
    "PMD.TooManyMethods", "PMD.ExcessivePublicCount",
    "ClassWithTooManyMethods", "OverlyComplexClass",
    "ClassTooDeepInInheritanceTree"})
public class PreparedStatementWrapper extends MeasuringPreparedStatement
    implements PreparedStatement {

    /**
     * Original PreparedStatement.
     */
    private final PreparedStatement original;

    /**
     * Secondary constructor.
     * @param original Original Statement.
     * @param counter Query counter.
     * @param timer Query timer.
     */
    public PreparedStatementWrapper(final PreparedStatement original,
        final ThreadLocal<Integer> counter, final ThreadLocal<Long> timer
    ) {
        this(original, new QueryMeter(counter, timer));
    }

    /**
     * Primary constructor.
     * @param original Original PreparedStatement.
     * @param meter Query meter.
     */
    public PreparedStatementWrapper(final PreparedStatement original,
        final QueryMeter meter
    ) {
        super(original, meter);
        this.original = original;
    }

    @Override
    public final void setNull(final int index, final int type)
        throws SQLException {
        this.original.setNull(index, type);
    }

    @Override
    public final void setBoolean(final int index, final boolean value)
        throws SQLException {
        this.original.setBoolean(index, value);
    }

    @Override
    public final void setByte(final int index, final byte value)
        throws SQLException {
        this.original.setByte(index, value);
    }

    @Override
    public final void setShort(final int index, final short value)
        throws SQLException {
        this.original.setShort(index, value);
    }

    @Override
    public final void setInt(final int index, final int value)
        throws SQLException {
        this.original.setInt(index, value);
    }

    @Override
    public final void setLong(final int index, final long value)
        throws SQLException {
        this.original.setLong(index, value);
    }

    @Override
    public final void setFloat(final int index, final float value)
        throws SQLException {
        this.original.setFloat(index, value);
    }

    @Override
    public final void setDouble(final int index, final double value)
        throws SQLException {
        this.original.setDouble(index, value);
    }

    @Override
    public final void setBigDecimal(
        final int index, final BigDecimal value
    ) throws SQLException {
        this.original.setBigDecimal(index, value);
    }

    @Override
    public final void setString(final int index, final String value)
        throws SQLException {
        this.original.setString(index, value);
    }

    @Override
    public final void setBytes(final int index, final byte[] value)
        throws SQLException {
        this.original.setBytes(index, value);
    }

    @Override
    public final void setDate(final int index, final Date value)
        throws SQLException {
        this.original.setDate(index, value);
    }

    @Override
    public final void setTime(final int index, final Time value)
        throws SQLException {
        this.original.setTime(index, value);
    }

    @Override
    public final void setTimestamp(final int index, final Timestamp value)
        throws SQLException {
        this.original.setTimestamp(index, value);
    }

    @Override
    public final void setAsciiStream(
        final int index, final InputStream input, final int length
    ) throws SQLException {
        this.original.setAsciiStream(index, input, length);
    }

    // @checkstyle MissingDeprecated (3 lines)
    @Override
    @Deprecated(since = "1.2")
    public final void setUnicodeStream(
        final int index, final InputStream input, final int length
    ) throws SQLException {
        this.original.setUnicodeStream(index, input, length);
    }

    @Override
    public final void setBinaryStream(
        final int index, final InputStream input, final int length
    ) throws SQLException {
        this.original.setBinaryStream(index, input, length);
    }

    @Override
    public final void clearParameters() throws SQLException {
        this.original.clearParameters();
    }

    @Override
    public final void setObject(
        final int index, final Object value, final int type
    ) throws SQLException {
        this.original.setObject(index, value, type);
    }

    @Override
    public final void setObject(final int index, final Object value)
        throws SQLException {
        this.original.setObject(index, value);
    }

    @Override
    public final void addBatch() throws SQLException {
        this.original.addBatch();
    }

    @Override
    public final void setCharacterStream(
        final int index, final Reader reader, final int length
    ) throws SQLException {
        this.original.setCharacterStream(index, reader, length);
    }

    @Override
    public final void setRef(final int index, final Ref value)
        throws SQLException {
        this.original.setRef(index, value);
    }

    @Override
    public final void setBlob(final int index, final Blob value)
        throws SQLException {
        this.original.setBlob(index, value);
    }

    @Override
    public final void setClob(final int index, final Clob value)
        throws SQLException {
        this.original.setClob(index, value);
    }

    @Override
    public final void setArray(final int index, final Array value)
        throws SQLException {
        this.original.setArray(index, value);
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        return this.original.getMetaData();
    }

    @Override
    public final void setDate(
        final int index, final Date value, final Calendar cal
    ) throws SQLException {
        this.original.setDate(index, value, cal);
    }

    @Override
    public final void setTime(
        final int index, final Time time, final Calendar cal
    ) throws SQLException {
        this.original.setTime(index, time, cal);
    }

    @Override
    public final void setTimestamp(
        final int index, final Timestamp time, final Calendar cal
    ) throws SQLException {
        this.original.setTimestamp(index, time, cal);
    }

    @Override
    public final void setNull(
        final int index, final int type, final String name
    ) throws SQLException {
        this.original.setNull(index, type, name);
    }

    @Override
    public final void setURL(final int index, final URL url)
        throws SQLException {
        this.original.setURL(index, url);
    }

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        return this.original.getParameterMetaData();
    }

    @Override
    public final void setRowId(final int index, final RowId row)
        throws SQLException {
        this.original.setRowId(index, row);
    }

    @Override
    public final void setNString(final int index, final String value)
        throws SQLException {
        this.original.setNString(index, value);
    }

    @Override
    public final void setNCharacterStream(
        final int index, final Reader value, final long length
    ) throws SQLException {
        this.original.setNCharacterStream(index, value, length);
    }

    @Override
    public final void setNClob(final int index, final NClob value)
        throws SQLException {
        this.original.setNClob(index, value);
    }

    @Override
    public final void setClob(
        final int index, final Reader reader, final long length
    ) throws SQLException {
        this.original.setClob(index, reader, length);
    }

    @Override
    public final void setBlob(
        final int index, final InputStream input, final long length
    ) throws SQLException {
        this.original.setBlob(index, input, length);
    }

    @Override
    public final void setNClob(
        final int index, final Reader reader, final long length)
        throws SQLException {
        this.original.setNClob(index, reader, length);
    }

    @Override
    public final void setSQLXML(final int index, final SQLXML object)
        throws SQLException {
        this.original.setSQLXML(index, object);
    }

    // @checkstyle ParameterNumber (3 lines)
    @Override
    public final void setObject(
        final int index, final Object object, final int type, final int scale
    ) throws SQLException {
        this.original.setObject(index, object, type, scale);
    }

    @Override
    public final void setAsciiStream(
        final int index, final InputStream input, final long length
    ) throws SQLException {
        this.original.setAsciiStream(index, input, length);
    }

    @Override
    public final void setBinaryStream(
        final int index, final InputStream input, final long length
    ) throws SQLException {
        this.original.setBinaryStream(index, input, length);
    }

    @Override
    public final void setCharacterStream(
        final int index, final Reader reader, final long length
    ) throws SQLException {
        this.original.setCharacterStream(index, reader, length);
    }

    @Override
    public final void setAsciiStream(final int index, final InputStream input)
        throws SQLException {
        this.original.setAsciiStream(index, input);
    }

    @Override
    public final void setBinaryStream(final int index, final InputStream input)
        throws SQLException {
        this.original.setBinaryStream(index, input);
    }

    @Override
    public final void setCharacterStream(final int index, final Reader reader)
        throws SQLException {
        this.original.setCharacterStream(index, reader);
    }

    @Override
    public final void setNCharacterStream(final int index, final Reader value)
        throws SQLException {
        this.original.setNCharacterStream(index, value);
    }

    @Override
    public final void setClob(final int index, final Reader reader)
        throws SQLException {
        this.original.setClob(index, reader);
    }

    @Override
    public final void setBlob(final int index, final InputStream input)
        throws SQLException {
        this.original.setBlob(index, input);
    }

    @Override
    public final void setNClob(final int index, final Reader reader)
        throws SQLException {
        this.original.setNClob(index, reader);
    }

    // @checkstyle ParameterNumber (3 lines)
    @Override
    public final void setObject(final int index, final Object object,
        final SQLType type, final int scale
    ) throws SQLException {
        this.original.setObject(index, object, type, scale);
    }

    @Override
    public final void setObject(
        final int index, final Object object, final SQLType type
    ) throws SQLException {
        this.original.setObject(index, object, type);
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
