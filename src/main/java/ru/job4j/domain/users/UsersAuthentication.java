package ru.job4j.domain.users;

import java.util.Collections;
import java.util.Map;

public class UsersAuthentication {
    private final Users users;

    public UsersAuthentication(final Users users) {
        this.users = users;
    }

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
