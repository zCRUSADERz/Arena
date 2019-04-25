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

import java.sql.SQLException;

/**
 * Query meter.
 * Counts the number of queries to the database and the time of their execution.
 *
 * @since 0.1
 */
public class QueryMeter {

    /**
     * Counter to count the number of queries to the database.
     */
    private final ThreadLocal<Integer> counter;

    /**
     * Timer to count the execution time of queries to the database.
     * In milliseconds.
     */
    private final ThreadLocal<Long> timer;

    /**
     * Primary constructor.
     * @param counter Query counter.
     * @param timer Query timer.
     */
    public QueryMeter(
        final ThreadLocal<Integer> counter, final ThreadLocal<Long> timer
    ) {
        this.counter = counter;
        this.timer = timer;
    }

    /**
     * Measure the time of the query to the database.
     * @param query SQL query.
     * @param <T> Result of query execution;
     * @return Result of query execution.
     * @throws SQLException if SQLException was thrown while executing the query.
     */
    public final <T> T measure(final SQLQuery<T> query) throws SQLException {
        final T result;
        final int count =  this.counter.get() + 1;
        this.counter.set(count);
        final long start = System.currentTimeMillis();
        result = query.execute();
        final long finish = System.currentTimeMillis();
        final long spent = finish - start;
        final long time = this.timer.get() + spent;
        this.timer.set(time);
        return result;
    }
}
