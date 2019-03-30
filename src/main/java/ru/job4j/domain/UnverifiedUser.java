package ru.job4j.domain;

import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UnverifiedUser implements User {
    private final HttpServletRequest request;
    private final UncheckedScalar<Map<String, String>> validateErrors;

    public UnverifiedUser(final HttpServletRequest request) {
        this.request = request;
        this.validateErrors = new UncheckedScalar<>(
                new StickyScalar<>(
                        () -> new RequestUser(request).validate()
                )
        );
    }

    @Override
    public final String name() {
        if (!this.isValid()) {
            throw new IllegalStateException("Request user invalid!");
        }
        return this.request.getParameter("name");
    }

    @Override
    public final String password() {
        if (!this.isValid()) {
            throw new IllegalStateException("Request user invalid!");
        }
        return this.request.getParameter("password");
    }

    public final boolean isValid() {
        return this.validateErrors.value().isEmpty();
    }

    public final Map<String, String> validateErrors() {
        return this.validateErrors.value();
    }

    public static class RequestUser {
        private final HttpServletRequest request;

        public RequestUser(final HttpServletRequest request) {
            this.request = request;
        }

        public final Map<String, String> validate() {
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
