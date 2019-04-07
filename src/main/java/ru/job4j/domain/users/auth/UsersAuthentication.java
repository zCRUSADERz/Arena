package ru.job4j.domain.users.auth;

import java.util.Map;

public interface UsersAuthentication {

    Map<String, String> authorize(final UnverifiedUser unverifiedUser);
}
