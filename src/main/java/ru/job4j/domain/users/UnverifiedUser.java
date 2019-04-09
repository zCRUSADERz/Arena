package ru.job4j.domain.users;

import ru.job4j.domain.users.auth.UserAuthorization;
import ru.job4j.domain.users.auth.UserAuthorizationFactory;
import ru.job4j.domain.users.auth.UserValidateErrors;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Unverified user.
 * User from http request with unverified data.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class UnverifiedUser {
    private final HttpServletRequest request;
    private final MessageDigest messageDigest;
    private final UserAuthorizationFactory userAuthorizationFactory;

    public UnverifiedUser(final HttpServletRequest request,
                          final MessageDigest messageDigest,
                          final UserAuthorizationFactory userAuthorizationFactory) {
        this.request = request;
        this.messageDigest = messageDigest;
        this.userAuthorizationFactory = userAuthorizationFactory;
    }

    /**
     * Authorize user.
     * If user with same credentials not found, then will be registered new user.
     * @return Map with attribute->error message.
     * If empty map, then user authenticated.
     */
    public final Map<String, String> authorize() {
        final Map<String, String> result;
        final Map<String, String> validateErrors = new UserValidateErrors(
                this.request
        ).errors();
        if (validateErrors.isEmpty()) {
            final String userName = this.request.getParameter("name");
            final String password = this.request.getParameter("password");
            final byte[] passwordHash = this.messageDigest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );
            final UserAuthorization authorization = this.userAuthorizationFactory
                    .authorizationFor(userName, passwordHash);
            result = authorization.authorize();
        } else {
            result = validateErrors;
        }
        return result;
    }
}
