package ru.job4j.db.transactions;

import ru.job4j.domain.duels.Duels;
import ru.job4j.domain.duels.results.DuelAttackResult;

public class DuelCreateTransaction implements Duels {
    private final Transaction transaction;
    private final Duels origin;

    public DuelCreateTransaction(final Transaction transaction,
                                 final Duels origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    @Override
    public final void create(final String first, final String second) {
        this.transaction.start();
        this.origin.create(first, second);
        this.transaction.commit();
    }

    @Override
    public final void userTurn(final int duelID, final String userName) {
        this.origin.userTurn(duelID, userName);
    }

    @Override
    public final void finished(final DuelAttackResult attackResult) {
        this.origin.finished(attackResult);
    }
}
