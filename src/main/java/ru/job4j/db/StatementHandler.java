package ru.job4j.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Set;

/**
 * Statement handler.
 * Handler for Statement and PreparedStatement.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class StatementHandler implements InvocationHandler {
    private final Statement original;
    private final Set<String> handleMethods;
    /**
     * Counter to count the number of queries to the database.
     */
    private final ThreadLocal<Integer> queryCounter;
    /**
     * Timer to count the execution time of queries to the database.
     * In milliseconds.
     */
    private final ThreadLocal<Long> queryTimer;

    public StatementHandler(final Statement original,
                            final ThreadLocal<Integer> queryCounter,
                            final ThreadLocal<Long> queryTimer) {
        this(
                original,
                Set.of("executeQuery", "executeUpdate", "execute"),
                queryCounter,
                queryTimer
        );
    }

    public StatementHandler(final Statement original,
                            final Set<String> handleMethods,
                            final ThreadLocal<Integer> queryCounter,
                            final ThreadLocal<Long> queryTimer) {
        this.original = original;
        this.handleMethods = handleMethods;
        this.queryCounter = queryCounter;
        this.queryTimer = queryTimer;
    }

    /**
     * Counts the number of queries to the database
     * and the time of their execution.
     * @param proxy proxy.
     * @param method method.
     * @param args args.
     * @return result of original statement.
     * @throws IllegalAccessException IllegalAccessException.
     * @throws IllegalArgumentException IllegalArgumentException.
     * @throws InvocationTargetException InvocationTargetException.
     */
    public final Object invoke(Object proxy, Method method, Object[] args)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (this.handleMethods.contains(method.getName())) {
            this.queryCounter.set(this.queryCounter.get() + 1);
            final long start = System.currentTimeMillis();
            final Object result = method.invoke(original, args);
            final long finish = System.currentTimeMillis();
            final long time = finish - start;
            this.queryTimer.set(this.queryTimer.get() + time);
            return result;
        } else {
            return method.invoke(original, args);
        }
    }
}
