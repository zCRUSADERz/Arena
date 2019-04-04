package ru.job4j.db.factories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class ConnectionFactory implements Supplier<Connection> {
    private final DataSource dataSource;

    public ConnectionFactory(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public final Connection get() {
        try {
            return this.dataSource.getConnection();
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
