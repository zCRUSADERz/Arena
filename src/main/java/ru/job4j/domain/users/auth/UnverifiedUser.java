package ru.job4j.domain.users.auth;

import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import ru.job4j.domain.users.UserCredentials;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Unverified user.
 * User from http request with unverified data.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class UnverifiedUser implements UserCredentials {
    private final HttpServletRequest request;
    /**
     * A caching mechanism for obtaining a validation result once.
     */
    private final UncheckedScalar<Map<String, String>> validateErrors;
    private final MessageDigest messageDigest;

    public UnverifiedUser(final HttpServletRequest request,
                          final MessageDigest messageDigest) {
        this.request = request;
        this.validateErrors = new UncheckedScalar<>(
                new StickyScalar<>(new UserValidateErrors(request))
        );
        this.messageDigest = messageDigest;
    }

    /**
     * @return user name.
     * @throws IllegalStateException if user is not valid.
     */
    @Override
    public final String name() {
        if (!this.isValid()) {
            throw new IllegalStateException("Request user invalid!");
        }
        return this.request.getParameter("name");
    }

    /**
     * @return password hash.
     * @throws IllegalStateException if user is not valid.
     */
    @Override
    public final byte[] password() {
        if (!this.isValid()) {
            throw new IllegalStateException("Request user invalid!");
        }
        final String password = this.request.getParameter("password");
        return messageDigest.digest(
                password.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * Check user valid or not.
     * @return true, if validation was successful.
     */
    public final boolean isValid() {
        return this.validateErrors.value().isEmpty();
    }

    /**
     * Validate user data.
     * @return Map with attribute->error message.
     */
    public final Map<String, String> validate() {
        return this.validateErrors.value();
    }

    public final static class UserValidateErrors
            implements Scalar<Map<String, String>> {
        private final HttpServletRequest request;

        private UserValidateErrors(final HttpServletRequest request) {
            this.request = request;
        }

        /**
         * User validation.
         * @return Map with attribute->error message.
         */
        @Override
        public final Map<String, String> value() {
            final Map<String, String> errors = new HashMap<>();
            final Map<String, String[]> parameters = request.getParameterMap();
            if (parameters.containsKey("name")) {
                if (parameters.get("name")[0].isBlank()) {
                    errors.put("name", "User name can't be blank.");
                }
            } else {
                errors.put("name", "User name missing.");
            }
            if (parameters.containsKey("password")) {
                if (parameters.get("password")[0].length() < 6) {
                    errors.put(
                            "password",
                            "Password is weak. Password length at least 6 characters."
                    );
                }
            } else {
                errors.put("password", "Password missing.");
            }
            return errors;
        }
    }
}
