package ru.job4j.domain.users;

public class AuthenticationResult {
    private final boolean registered;
    private final boolean authResult;

    public AuthenticationResult(final boolean registered,
                                final boolean authResult) {
        this.registered = registered;
        this.authResult = authResult;
    }

    public final boolean isRegistered() {
        return this.registered;
    }

    public final boolean wrongCredentials() {
        return this.authResult;
    }
}
