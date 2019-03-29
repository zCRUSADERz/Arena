package ru.job4j.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.Set;

public class StatementHandler implements InvocationHandler {
    private final Statement original;
    private final Set<String> handleMethods;
    private final ThreadLocal<Integer> queryCounter;
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
