package ru.job4j.domain.users.auth;

import java.util.Map;

public interface UserAuthorization {

    /**
     * Authorize user.
     * @return map with errors. If map empty, then user authorized.
     * Map(userAttribute -> error)
     */
    Map<String, String> authorize();
}
