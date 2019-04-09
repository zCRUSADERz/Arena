package ru.job4j.domain.users.auth;

public interface UserAuthorizationFactory {

    UserAuthorization authorizationFor(final String userName,
                                       final byte[] passwordHash);
}
