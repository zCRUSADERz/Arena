package ru.job4j.domain.users.auth;

/**
 * Authentication result.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 6.04.2019
 */
public class AuthenticationResult {
    private final boolean registered;
    private final boolean authResult;

    public AuthenticationResult(final boolean registered,
                                final boolean authResult) {
        this.registered = registered;
        this.authResult = authResult;
    }

    /**
     * @return true, if registered.
     */
    public final boolean isRegistered() {
        return this.registered;
    }

    /**
     * @return true, if user name and password hash equals.
     */
    public final boolean wrongCredentials() {
        return this.authResult;
    }
}
