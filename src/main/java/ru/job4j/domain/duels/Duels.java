package ru.job4j.domain.duels;

import ru.job4j.domain.duels.results.DuelAttackResult;

/**
 * Duels.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public interface Duels {

    /**
     * Create new active duel with this duelists.
     * @param first first duelist name.
     * @param second second duelist name.
     */
    void create(final String first, final String second);

    /**
     * Close active duel based on attack results.
     * @param attackResult attack result.
     */
    void finished(final DuelAttackResult attackResult);

    /**
     * Run a user move for a duel with a passed id.
     * @param duelID active duel ID.
     * @param userName the name of the user who will perform the move.
     */
    void userTurn(final int duelID, final String userName);
}
