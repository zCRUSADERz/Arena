package ru.job4j.db.transactions;

import ru.job4j.domain.users.auth.UserAuthorization;

import java.util.Map;

/**
 * User authentication-registration transaction.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 07.04.2019
 */
public class AuthTransaction implements UserAuthorization {
    private final Transaction transaction;
    private final UserAuthorization origin;

    public AuthTransaction(final Transaction transaction,
                           final UserAuthorization origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    /**
     * Start and complete transaction for user authorization.
     * @return result of origin UsersAuthentication.
     */
    @Override
    public final Map<String, String> authorize() {
        this.transaction.start();
        final Map<String, String> result = this.origin.authorize();
        this.transaction.commit();
        return result;
    }
}
