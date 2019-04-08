package ru.job4j.db.transactions;

import ru.job4j.domain.duels.Duels;
import ru.job4j.domain.duels.results.DuelAttackResult;

/**
 * Transaction to create a new ActiveDuel.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 07.04.2019
 */
public class DuelCreateTransaction implements Duels {
    private final Transaction transaction;
    private final Duels origin;

    public DuelCreateTransaction(final Transaction transaction,
                                 final Duels origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    /**
     * Start and complete transaction to create a new ActiveDuel.
     * @param first first duelist name.
     * @param second second duelist name.
     */
    @Override
    public final void create(final String first, final String second) {
        this.transaction.start();
        this.origin.create(first, second);
        this.transaction.commit();
    }

    /**
     * User turn.
     * @param duelID duel ID.
     * @param userName user name.
     */
    @Override
    public final void userTurn(final int duelID, final String userName) {
        this.origin.userTurn(duelID, userName);
    }

    /**
     * Active duel completed.
     * @param attackResult attack result.
     */
    @Override
    public final void finished(final DuelAttackResult attackResult) {
        this.origin.finished(attackResult);
    }
}
