package ru.job4j.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class UsersAuthentication {
    private final Users users;

    public UsersAuthentication(final Users users) {
        this.users = users;
    }

    public final Map<String, String> authorize(final UnverifiedUser unverifiedUser) {
        final Map<String, String> errors;
        if (unverifiedUser.isValid()) {
            final Optional<User> optUser = this.users.user(unverifiedUser.name());
            if (optUser.isPresent()) {
                final User registeredUser = optUser.get();
                final UsersCredentials credentials = new UsersCredentials(
                        registeredUser, unverifiedUser
                );
                if (credentials.match()) {
                    errors = Collections.emptyMap();
                } else {
                    errors = Map.of("password", "Incorrect password.");
                }
            } else {
                errors = this.users.register(unverifiedUser);
            }
        } else {
            errors = unverifiedUser.validateErrors();
        }
        return errors;
    }
}
