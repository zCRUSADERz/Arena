package ru.job4j.db.transactions;

import ru.job4j.domain.users.auth.UnverifiedUser;
import ru.job4j.domain.users.auth.UsersAuthentication;

import java.util.Map;

public class AuthTransaction implements UsersAuthentication {
    private final Transaction transaction;
    private final UsersAuthentication origin;

    public AuthTransaction(final Transaction transaction,
                           final UsersAuthentication origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    @Override
    public final Map<String, String> authorize(
            final UnverifiedUser unverifiedUser) {
        this.transaction.start();
        final Map<String, String> result = this.origin.authorize(unverifiedUser);
        this.transaction.commit();
        return result;
    }
}
