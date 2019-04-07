package ru.job4j;

import ru.job4j.db.*;
import ru.job4j.db.transactions.AuthTransaction;
import ru.job4j.db.transactions.DuelCreateTransaction;
import ru.job4j.db.transactions.Transaction;
import ru.job4j.domain.duels.ActiveDuels;
import ru.job4j.domain.duels.Duels;
import ru.job4j.domain.duels.DuelsSimple;
import ru.job4j.domain.duels.FinishedDuels;
import ru.job4j.domain.duels.duel.ActiveDuel;
import ru.job4j.domain.duels.duel.FinishedDuel;
import ru.job4j.domain.duels.duelists.ActiveDuelist;
import ru.job4j.domain.duels.duelists.FinishedDuelist;
import ru.job4j.domain.duels.logs.FinalBlow;
import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.duels.logs.FinishedDuelLog;
import ru.job4j.domain.queue.UsersQueue;
import ru.job4j.domain.queue.UsersQueueConsumer;
import ru.job4j.domain.rating.UserRating;
import ru.job4j.domain.users.User;
import ru.job4j.domain.users.Users;
import ru.job4j.domain.users.auth.MessageDigestFactory;
import ru.job4j.domain.users.auth.UnverifiedUser;
import ru.job4j.domain.users.auth.UsersAuthentication;
import ru.job4j.domain.users.auth.UsersAuthenticationSimple;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;
import java.util.function.IntFunction;

public class DependencyContainer {
    private final static int TURN_DURATION;
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
    private final static IntFunction<ActiveDuel> ACTIVE_DUEL_FACTORY;

    static {
        TURN_DURATION = 10000;
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
        final Transaction transaction = new Transaction(CONNECTION_HOLDER);
        ACTIVE_DUEL_FACTORY = duelID ->
            new ActiveDuel(
                    duelID,
                    CONNECTION_HOLDER,
                    duelStartDelay,
                    userName ->
                            new ActiveDuelist(
                                    userName,
                                    CONNECTION_HOLDER,
                                    TURN_DURATION
                            )
            );
        USERS_FACTORY = httpRequest -> new UnverifiedUser(
                httpRequest,
                new MessageDigestFactory(passSalt).messageDigest()
        );
        USERS_AUTHENTICATION = new AuthTransaction(
                transaction,
                new UsersAuthenticationSimple(
                        new Users(CONNECTION_HOLDER)
                )
        );
        DUELS = new DuelCreateTransaction(
                transaction,
                new DuelsSimple(
                        CONNECTION_HOLDER,
                        ACTIVE_DUEL_FACTORY,
                        new FinalBlows(CONNECTION_HOLDER),
                        userName -> new User(userName, CONNECTION_HOLDER)
                )
        );
        USERS_QUEUE = new UsersQueue();
        new Thread(
                new UsersQueueConsumer(
                        usersQueue(),
                        DUELS,
                        defaultUserName
                )
        ).start();
        ACTIVE_DUELS = new ActiveDuels(CONNECTION_HOLDER, DUELS);
        FINISHED_DUELS = new FinishedDuels(
                CONNECTION_HOLDER,
                duelID -> new FinishedDuel(
                        duelID,
                        CONNECTION_HOLDER,
                        userName -> new FinishedDuelist(
                                userName,
                                duelID,
                                CONNECTION_HOLDER
                        ),
                        innerDuelID -> new FinishedDuelLog(
                                innerDuelID,
                                CONNECTION_HOLDER,
                                new FinalBlow(
                                        innerDuelID,
                                        CONNECTION_HOLDER
                                )
                        )
                )
        );
        USERS_RATING = userName -> new UserRating(userName, CONNECTION_HOLDER);
    }

    public static int turnDuration() {
        return TURN_DURATION;
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

    public static Function<HttpServletRequest, UnverifiedUser> usersFactory() {
        return USERS_FACTORY;
    }

    public static ConnectionHolder connectionHolder() {
        return CONNECTION_HOLDER;
    }

    public static IntFunction<ActiveDuel> activeDuelFactory() {
        return ACTIVE_DUEL_FACTORY;
    }

    public static FinishedDuels finishedDuels() {
        return FINISHED_DUELS;
    }
}
