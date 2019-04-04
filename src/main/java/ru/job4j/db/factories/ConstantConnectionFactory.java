package ru.job4j.db.factories;

import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.function.Supplier;

public class ConstantConnectionFactory implements Supplier<Connection> {
    private final UncheckedScalar<Connection> connection;

    public ConstantConnectionFactory(final DataSource dataSource) {
        this(new UncheckedScalar<>(
                new StickyScalar<>(
                        dataSource::getConnection
                )
        ));
    }

    public ConstantConnectionFactory(final UncheckedScalar<Connection> connection) {
        this.connection = connection;
    }



    @Override
    public final Connection get() {
        return this.connection.value();
    }
}
