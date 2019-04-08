package ru.job4j.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.function.Function;

/**
 * Connection proxy.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 30.03.2019
 */
public class ConnectionHandler implements InvocationHandler {
    private final Connection original;
    private final Function<Statement, InvocationHandler> handlerFactory;

    public ConnectionHandler(
            final Connection original,
            final Function<Statement, InvocationHandler> handlerFactory) {
        this.original = original;
        this.handlerFactory = handlerFactory;
    }

    /**
     * Wraps Statement and PreparedStatement in special implementations.
     * @param proxy proxy.
     * @param method method.
     * @param args args.
     * @return result of origin Connection.
     * @throws IllegalAccessException IllegalAccessException.
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws InvocationTargetException InvocationTargetException.
     */
    public final Object invoke(final Object proxy, final Method method,
                               final Object[] args)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        final Object result;
        if (method.getName().equals("createStatement")) {
            result = Proxy.newProxyInstance(
                    Statement.class.getClassLoader(),
                    new Class[] {Statement.class},
                    this.handlerFactory.apply(
                            (Statement) method.invoke(this.original, args)
                    )
            );
        } else if (method.getName().equals("prepareStatement")) {
            result = Proxy.newProxyInstance(
                    PreparedStatement.class.getClassLoader(),
                    new Class[] {PreparedStatement.class},
                    this.handlerFactory.apply(
                            (PreparedStatement) method.invoke(this.original, args)
                    )
            );
        } else {
            result = method.invoke(this.original, args);
        }
        return result;
    }
}
