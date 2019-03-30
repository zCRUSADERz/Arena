package ru.job4j;

import com.zaxxer.hikari.HikariDataSource;
import ru.job4j.db.ConnectionHandler;
import ru.job4j.db.DBConfig;
import ru.job4j.db.DataSourceWrapper;
import ru.job4j.db.StatementHandler;
import ru.job4j.domain.Users;
import ru.job4j.domain.UsersAuthentication;

import javax.sql.DataSource;

public class DependencyContainer {
    private final static HikariDataSource DB_SOURCE;
    private final static ThreadLocal<Integer> QUERY_COUNTER;
    private final static ThreadLocal<Long> QUERY_TIMER;
    private final static ThreadLocal<Long> REQUEST_TIMER;
    private final static UsersAuthentication USERS_AUTHENTICATION;

    static {
        QUERY_COUNTER = ThreadLocal.withInitial(() -> 0);
        QUERY_TIMER = ThreadLocal.withInitial(() -> 0L);
        REQUEST_TIMER = ThreadLocal.withInitial(() -> 0L);
        DB_SOURCE = new DataSourceWrapper(
                new DBConfig().config(),
                connection -> new ConnectionHandler(
                        connection,
                        statement -> new StatementHandler(
                                statement,
                                QUERY_COUNTER,
                                QUERY_TIMER
                        )
                )
        );
        USERS_AUTHENTICATION = new UsersAuthentication(
                new Users(
                        DB_SOURCE
                )
        );
    }

    public static ThreadLocal<Long> requestTimer() {
        return REQUEST_TIMER;
    }

    public static ThreadLocal<Integer> queryCounter() {
        return QUERY_COUNTER;
    }

    public static ThreadLocal<Long> queryTimer() {
        return QUERY_TIMER;
    }

    public static UsersAuthentication usersAuthentication() {
        return USERS_AUTHENTICATION;
    }
}
