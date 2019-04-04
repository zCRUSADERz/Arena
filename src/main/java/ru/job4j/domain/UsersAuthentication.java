package ru.job4j.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class UsersAuthentication {
    private final Supplier<Users> usersFactory;

    public UsersAuthentication(final Supplier<Users> usersFactory) {
        this.usersFactory = usersFactory;
    }

    public final Map<String, String> authorize(final UnverifiedUser unverifiedUser) {
        final Map<String, String> errors;
        try (final Users users = this.usersFactory.get()) {
            if (unverifiedUser.isValid()) {
                final Optional<User> optUser = users.user(unverifiedUser.name());
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
                    errors = users.register(unverifiedUser);
                }
            } else {
                errors = unverifiedUser.validateErrors();
            }
        } catch (final Exception ex) {
            throw new IllegalStateException(ex);
        }
        return errors;
    }
}
