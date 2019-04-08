package ru.job4j.domain.users.auth;

import ru.job4j.domain.users.Users;

import java.util.Collections;
import java.util.Map;

/**
 * Users authentication simple.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public class UsersAuthenticationSimple implements UsersAuthentication {
    private final Users users;

    public UsersAuthenticationSimple(final Users users) {
        this.users = users;
    }

    /**
     * {@inheritDoc}
     */
    public final Map<String, String> authorize(final UnverifiedUser unverifiedUser) {
        final Map<String, String> errors;
        if (unverifiedUser.isValid()) {
            final AuthenticationResult authResult
                    = this.users.authorize(unverifiedUser);
            if (authResult.isRegistered()) {
                if (authResult.wrongCredentials()) {
                    errors = Map.of("password", "Incorrect password.");
                } else {
                    errors = Collections.emptyMap();
                }
            } else {
                if (this.users.register(unverifiedUser)) {
                    errors = Collections.emptyMap();
                } else {
                    errors = Map.of("name", "Username is already taken");
                }
            }
        } else {
            errors = unverifiedUser.validate();
        }
        return errors;
    }
}
