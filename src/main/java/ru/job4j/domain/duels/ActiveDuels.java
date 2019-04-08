package ru.job4j.domain.duels;

import java.util.Optional;

/**
 * Active duels.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public interface ActiveDuels {

    /**
     * Checks if this user is in active duel.
     * If the user in an active duel, then the attack action will be returned.
     * @param userName user name.
     * @return optional of AttackAction.
     */
    Optional<AttackAction> inDuel(final String userName);
}
