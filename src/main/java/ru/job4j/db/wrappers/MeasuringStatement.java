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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Measuring Statement.
 * Measures the number of queries to the database and their execution time.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public class MeasuringStatement {

    /**
     * Original Statement.
     */
    private final Statement original;

    /**
     * Query execution time meter.
     */
    private final QueryMeter measurer;

    /**
     * Primary constructor.
     * @param original Original Statement.
     * @param measurer Query execution time meter.
     */
    public MeasuringStatement(
        final Statement original, final QueryMeter measurer
    ) {
        this.original = original;
        this.measurer = measurer;
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final boolean execute(final String sql) throws SQLException {
        return this.measurer.measure(() -> this.original.execute(sql));
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param keys Auto generated keys.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final boolean execute(final String sql, final int keys)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.execute(sql, keys)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final boolean execute(final String sql, final int... columns)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.execute(sql, columns)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final boolean execute(final String sql, final String... columns)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.execute(sql, columns)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final ResultSet executeQuery(final String sql) throws SQLException {
        // @checkstyle MethodBodyComments (1 lines)
        //noinspection JDBCResourceOpenedButNotSafelyClosed
        return this.measurer.measure(() -> this.original.executeQuery(sql));
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final int executeUpdate(final String sql) throws SQLException {
        return this.measurer.measure(() -> this.original.executeUpdate(sql));
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param keys Auto generated keys.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final int executeUpdate(final String sql, final int keys)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeUpdate(sql, keys)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final int executeUpdate(final String sql, final int... columns)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeUpdate(sql, columns)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final int executeUpdate(final String sql, final String... columns)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeUpdate(sql, columns)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final int[] executeBatch() throws SQLException {
        return this.measurer.measure(this.original::executeBatch);
    }

    /**
     * Invoke original Statement and measures execution time.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final long[] executeLargeBatch() throws SQLException {
        return this.measurer.measure(this.original::executeLargeBatch);
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final long executeLargeUpdate(final String sql) throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeLargeUpdate(sql)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param keys Auto generated keys.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final long executeLargeUpdate(final String sql, final int keys)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeLargeUpdate(sql, keys)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final long executeLargeUpdate(final String sql, final int... columns)
        throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeLargeUpdate(sql, columns)
        );
    }

    /**
     * Invoke original Statement and measures execution time.
     * @param sql SQL query.
     * @param columns Column indexes.
     * @return Result of calling the original statement.
     * @throws SQLException if original throw SQLException.
     * @see Statement
     */
    public final long executeLargeUpdate(
        final String sql, final String... columns
    ) throws SQLException {
        return this.measurer.measure(
            () -> this.original.executeLargeUpdate(sql, columns)
        );
    }
}
