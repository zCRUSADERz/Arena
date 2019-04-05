package ru.job4j.domain.users;

import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class UnverifiedUser implements UserCredentials {
    private final HttpServletRequest request;
    private final UncheckedScalar<Map<String, String>> validateErrors;
    private final MessageDigest messageDigest;

    public UnverifiedUser(final HttpServletRequest request,
                          final MessageDigest messageDigest) {
        this.request = request;
        this.validateErrors = new UncheckedScalar<>(
                new StickyScalar<>(new ValidateErrors(request))
        );
        this.messageDigest = messageDigest;
    }

    @Override
    public final String name() {
        if (!this.isValid()) {
            throw new IllegalStateException("Request user invalid!");
        }
        return this.request.getParameter("name");
    }

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

    public final boolean isValid() {
        return this.validateErrors.value().isEmpty();
    }

    public final Map<String, String> validate() {
        return this.validateErrors.value();
    }

    public final static class ValidateErrors
            implements Scalar<Map<String, String>> {
        private final HttpServletRequest request;

        private ValidateErrors(final HttpServletRequest request) {
            this.request = request;
        }

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
