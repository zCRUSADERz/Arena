package ru.job4j.domain.users.auth;

import java.util.Map;

/**
 * Users authentication.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public interface UsersAuthentication {

    /**
     * Authenticate user.
     * @param unverifiedUser user.
     * @return Map with attribute->error message.
     * If empty map, then user authenticated.
     */
    Map<String, String> authorize(final UnverifiedUser unverifiedUser);
}
