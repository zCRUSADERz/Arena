package ru.job4j.domain.users;

/**
 * User credentials.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 5.04.2019
 */
public interface UserCredentials {

    /**
     * User name.
     * @return user name.
     */
    String name();

    /**
     * Password hash.
     * @return password hash.
     */
    byte[] password();
}
