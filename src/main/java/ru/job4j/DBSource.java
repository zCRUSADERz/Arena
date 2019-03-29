package ru.job4j;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class DBSource {
    private final HikariDataSource source;

    public DBSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/Arena");
        config.setUsername("root");
        config.setPassword("password123A");
        config.setMaximumPoolSize(10);
        config.setAutoCommit(true);
        this.source = new DataSourceWrapper(new HikariDataSource(config));

    }

    public final DataSource source() {
        return this.source;
    }

    public static class DataSourceWrapper extends HikariDataSource {

        public DataSourceWrapper(final HikariConfig config) {
            super(config);
        }

        @Override
        public final Connection getConnection() throws SQLException {
            InvocationHandler handler = new ConnectionHandler(super.getConnection());
            return (Connection) Proxy.newProxyInstance(
                    Connection.class.getClassLoader(),
                    new Class[] { Connection.class },
                    handler
            );

        }
    }

    public static class ConnectionHandler implements InvocationHandler {
        private final Connection original;

        public ConnectionHandler(final Connection original) {
            this.original = original;
        }
        public final Object invoke(Object proxy, Method method, Object[] args)
                throws IllegalAccessException, IllegalArgumentException,
                InvocationTargetException {
            final Object result;
            System.out.println(String.format("Вызван метод: %s", method.getName()));
            if (method.getName().equals("createStatement")) {
                result = method.invoke(original, args);
            } else if (method.getName().equals("prepareStatement")) {
                result = method.invoke(original, args);
            } else {
                result = method.invoke(original, args);
            }
            return result;
        }
    }
}
