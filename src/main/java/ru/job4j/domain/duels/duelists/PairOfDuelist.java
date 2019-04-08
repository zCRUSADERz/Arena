package ru.job4j.domain.duels.duelists;


import java.util.HashMap;
import java.util.Map;

/**
 * Pair of duelist.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 6.04.2019
 */
public class PairOfDuelist<T extends SimpleDuelist> {
    private final T first;
    private final T second;

    public PairOfDuelist(final T first, final T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * Includes: duelists attributes.
     * @param userName prepares for user.
     * @return duelist attributes.
     */
    public final Map<String, String> attributesFor(final String userName) {
        final Map<String, String> result
                = new HashMap<>(this.first.attributesFor(userName));
        result.putAll(this.second.attributesFor(userName));
        return result;
    }

    /**
     * @param userName user name.
     * @return duelist with user name.
     */
    public final T duelist(final String userName) {
        final T result;
        if (this.first.name().equals(userName)) {
            result = this.first;
        } else if (this.second.name().equals(userName)) {
            result = this.second;
        } else {
            throw new IllegalStateException(String.format(
                    "User: %s, not found in this pair.",
                    userName
            ));
        }
        return result;
    }

    /**
     * @param userName user name.
     * @return opponent duelist for user.
     */
    public final T opponent(final String userName) {
        final T result;
        if (!this.first.name().equals(userName)) {
            result = this.first;
        } else {
            result = this.second;
        }
        return result;
    }
}
