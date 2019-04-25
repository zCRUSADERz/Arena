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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Measuring PreparedStatement.
 * Measures the number of queries to the database and their execution time.
 *
 * @since 0.1
 */
public class MeasuringPreparedStatement extends MeasuringStatement {

    /**
     * Original PreparedStatement.
     */
    private final PreparedStatement original;

    /**
     * Query execution time meter.
     */
    private final QueryMeter measurer;

    /**
     * Primary constructor.
     * @param original Original PreparedStatement.
     * @param measurer Query execution time meter.
     */
    public MeasuringPreparedStatement(
        final PreparedStatement original, final QueryMeter measurer
    ) {
        super(original, measurer);
        this.original = original;
        this.measurer = measurer;
    }

    /**
     * Invoke original PreparedStatement and measures execution time.
     * @return Result of calling the original.
     * @throws SQLException if original throw SQLException.
     * @see PreparedStatement
     */
    public final boolean execute() throws SQLException {
        return this.measurer.measure(this.original::execute);
    }

    /**
     * Invoke original PreparedStatement and measures execution time.
     * @return Result of calling the original.
     * @throws SQLException if original throw SQLException.
     * @see PreparedStatement
     */
    public final ResultSet executeQuery() throws SQLException {
        return this.measurer.measure(this.original::executeQuery);
    }

    /**
     * Invoke original PreparedStatement and measures execution time.
     * @return Result of calling the original.
     * @throws SQLException if original throw SQLException.
     * @see PreparedStatement
     */
    public final int executeUpdate() throws SQLException {
        return this.measurer.measure(this.original::executeUpdate);
    }

    /**
     * Invoke original PreparedStatement and measures execution time.
     * @return Result of calling the original.
     * @throws SQLException if original throw SQLException.
     * @see PreparedStatement
     */
    public final long executeLargeUpdate() throws SQLException {
        return this.measurer.measure(this.original::executeLargeUpdate);
    }
}
