package ru.job4j.db.transactions;

import ru.job4j.domain.duels.ActiveDuels;
import ru.job4j.domain.duels.AttackAction;

import java.util.Optional;

public class ActiveDuelsTransaction implements ActiveDuels {
    private final Transaction transaction;
    private final ActiveDuels origin;

    public ActiveDuelsTransaction(Transaction transaction, ActiveDuels origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    @Override
    public Optional<AttackAction> inDuel(final String userName) {
        this.transaction.start();
        final Optional<AttackAction> result = this.origin.inDuel(userName);
        if (result.isEmpty()) {
            this.transaction.commit();
        }
        return result;
    }
}
