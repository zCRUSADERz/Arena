package ru.job4j;

import ru.job4j.db.*;
import ru.job4j.domain.duels.ActiveDuels;
import ru.job4j.domain.duels.Duels;
import ru.job4j.domain.duels.FinishedDuels;
import ru.job4j.domain.duels.factories.SimpleDuelFactory;
import ru.job4j.domain.duels.factories.SimpleDuelistFactory;
import ru.job4j.domain.duels.logs.AttackLogs;
import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.duels.logs.GeneralDuelLog;
import ru.job4j.domain.queue.UsersQueue;
import ru.job4j.domain.queue.UsersQueueConsumer;
import ru.job4j.domain.rating.UserRating;
import ru.job4j.domain.users.MessageDigestFactory;
import ru.job4j.domain.users.UnverifiedUser;
import ru.job4j.domain.users.Users;
import ru.job4j.domain.users.UsersAuthentication;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

public class DependencyContainer {
    private final static ConnectionHolder CONNECTION_HOLDER;
    private final static ThreadLocal<Integer> QUERY_COUNTER;
    private final static ThreadLocal<Long> QUERY_TIMER;
    private final static ThreadLocal<Long> REQUEST_TIMER;
    private final static Function<HttpServletRequest, UnverifiedUser> USERS_FACTORY;
    private final static UsersAuthentication USERS_AUTHENTICATION;
    private final static Function<String, UserRating> USERS_RATING;
    private final static UsersQueue USERS_QUEUE;
    private final static Duels DUELS;
    private final static ActiveDuels ACTIVE_DUELS;
    private final static FinishedDuels FINISHED_DUELS;

    static {
        final int turnDuration = 10000;
        final int duelStartDelay = 30;
        final String defaultUserName = "";
        final String passSalt = "8w@8c4!48kww&0g";
        QUERY_COUNTER = ThreadLocal.withInitial(() -> 0);
        QUERY_TIMER = ThreadLocal.withInitial(() -> 0L);
        REQUEST_TIMER = ThreadLocal.withInitial(() -> 0L);
        CONNECTION_HOLDER = new ConnectionHolder(
                new DataSourceWrapper(
                        new DBConfig().config(),
                        connection -> new ConnectionHandler(
                                connection,
                                statement -> new StatementHandler(
                                        statement,
                                        QUERY_COUNTER,
                                        QUERY_TIMER
                                )
                        )
                ),
                new ThreadLocal<>(),
                ThreadLocal.withInitial(() -> false)
        );
        final ActiveDuels activeDuels = new ActiveDuels(
                CONNECTION_HOLDER,
                new SimpleDuelistFactory(
                        turnDuration,
                        CONNECTION_HOLDER
                ),
                new SimpleDuelFactory(
                        duelStartDelay,
                        new AttackLogs(
                                CONNECTION_HOLDER,
                                "attack_log"
                        )
                ),
                new AttackLogs(
                        CONNECTION_HOLDER,
                        "attack_log"
                )
        );
        final FinishedDuels finishedDuels = new FinishedDuels(
                CONNECTION_HOLDER,
                new FinalBlows(CONNECTION_HOLDER),
                new GeneralDuelLog(
                        new AttackLogs(
                                CONNECTION_HOLDER,
                                "attack_log_history"
                        ),
                        new FinalBlows(
                                CONNECTION_HOLDER
                        )
                )
        );
        USERS_FACTORY = httpRequest -> new UnverifiedUser(
                httpRequest,
                new MessageDigestFactory(passSalt).messageDigest()
        );
        USERS_AUTHENTICATION = new UsersAuthentication(new Users(CONNECTION_HOLDER));
        DUELS = new Duels(
                CONNECTION_HOLDER,
                activeDuels,
                finishedDuels
        );
        USERS_QUEUE = new UsersQueue();
        new Thread(
                new UsersQueueConsumer(
                        usersQueue(),
                        DUELS,
                        defaultUserName
                )
        ).start();
        ACTIVE_DUELS = activeDuels;
        FINISHED_DUELS = finishedDuels;
        USERS_RATING = userName -> new UserRating(userName, CONNECTION_HOLDER);
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

    public static ActiveDuels activeDuels() {
        return ACTIVE_DUELS;
    }

    public static Duels duels() {
        return DUELS;
    }

    public static Function<String, UserRating> usersRating() {
        return USERS_RATING;
    }

    public static FinishedDuels finishedDuels() {
        return FINISHED_DUELS;
    }

    public static Function<HttpServletRequest, UnverifiedUser> usersFactory() {
        return USERS_FACTORY;
    }

    public static ConnectionHolder connectionHolder() {
        return CONNECTION_HOLDER;
    }
}
