package ru.job4j;

import com.zaxxer.hikari.HikariDataSource;
import ru.job4j.db.ConnectionHandler;
import ru.job4j.db.DBConfig;
import ru.job4j.db.DataSourceWrapper;
import ru.job4j.db.StatementHandler;
import ru.job4j.db.factories.ConstantConnectionFactory;
import ru.job4j.domain.*;
import ru.job4j.domain.duels.*;
import ru.job4j.domain.duels.factories.SimpleDuelFactory;
import ru.job4j.domain.duels.factories.SimpleDuelistFactory;
import ru.job4j.domain.duels.logs.AttackLogs;
import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.queue.UsersQueue;
import ru.job4j.domain.queue.UsersQueueConsumer;
import ru.job4j.domain.rating.UsersRating;

import java.util.function.Supplier;

public class DependencyContainer {
    private final static HikariDataSource DB_SOURCE;
    private final static ThreadLocal<Integer> QUERY_COUNTER;
    private final static ThreadLocal<Long> QUERY_TIMER;
    private final static ThreadLocal<Long> REQUEST_TIMER;
    private final static UsersAuthentication USERS_AUTHENTICATION;
    private final static UsersQueue USERS_QUEUE;
    private final static Duels DUELS;
    private final static Supplier<ActiveDuels> ACTIVE_DUELS;
    private final static Supplier<UsersRating> USERS_RATING;

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
                () -> new Users(new ConstantConnectionFactory(DB_SOURCE))
        );
        DUELS = new Duels(
                DB_SOURCE,
                connection -> new ActiveDuels(
                        () -> connection,
                        new SimpleDuelistFactory(5000),
                        new SimpleDuelFactory(1),
                        (connectionInner, duelID) ->
                                new AttackLogs(connectionInner).logs(duelID)
                ),
                connection -> new FinishedDuels(
                        connection,
                        new FinalBlows(connection)
                ),
                connection -> new Users(() -> connection)
        );
        USERS_QUEUE = new UsersQueue();
        final String defaultUserName = "";
        new Thread(
                new UsersQueueConsumer(
                        usersQueue(),
                        DUELS,
                        defaultUserName
                )
        ).start();
        ACTIVE_DUELS = () -> new ActiveDuels(
                new ConstantConnectionFactory(DB_SOURCE),
                new SimpleDuelistFactory(5000),
                new SimpleDuelFactory(1),
                (connectionInner, duelID) ->
                        new AttackLogs(connectionInner).logs(duelID)
        );
        USERS_RATING = () -> new UsersRating(
                new ConstantConnectionFactory(DB_SOURCE)
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

    public static UsersQueue usersQueue() {
        return USERS_QUEUE;
    }

    public static Supplier<ActiveDuels> activeDuels() {
        return ACTIVE_DUELS;
    }

    public static Duels duels() {
        return DUELS;
    }

    public static Supplier<UsersRating> usersRating() {
        return USERS_RATING;
    }
}
