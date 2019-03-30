package ru.job4j.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class DataSourceWrapper extends HikariDataSource {
    private final Function<Connection, InvocationHandler> handlerFactory;

    public DataSourceWrapper(
            final HikariConfig config,
            final Function<Connection, InvocationHandler> handlerFactory) {
        super(config);
        this.handlerFactory = handlerFactory;
    }

    @Override
    public final Connection getConnection() throws SQLException {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                this.handlerFactory.apply(super.getConnection())
        );

    }
}
