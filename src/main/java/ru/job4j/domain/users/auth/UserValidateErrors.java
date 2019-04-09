package ru.job4j.domain.users.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class UserValidateErrors {
    private final HttpServletRequest request;

    public UserValidateErrors(final HttpServletRequest request) {
        this.request = request;
    }

    /**
     * User validation.
     * @return Map with attribute->error message.
     */
    public final Map<String, String> errors() {
        final Map<String, String> errors = new HashMap<>();
        final Map<String, String[]> parameters = request.getParameterMap();
        if (parameters.containsKey("name")) {
            final String userName = parameters.get("name")[0];
            if (userName.isBlank()) {
                errors.put("name", "User name can't be blank.");
            }
            if (userName.length() > 25) {
                errors.put(
                        "name",
                        "User name parameter is too long. "
                                + "Must be at most 25 characters."
                );
            }
        } else {
            errors.put("name", "User name missing.");
        }
        if (parameters.containsKey("password")) {
            final String password = parameters.get("password")[0];
            if (password.length() < 6) {
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
