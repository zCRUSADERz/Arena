package ru.job4j.domain.duels.duelists;

import java.util.Map;

/**
 * Simple duelists.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public interface SimpleDuelist {

    /**
     * @return duelist name
     */
    String name();

    /**
     * @param name user name.
     * @return duelist attributes, prepared for user.
     */
    Map<String, String> attributesFor(final String name);
}
