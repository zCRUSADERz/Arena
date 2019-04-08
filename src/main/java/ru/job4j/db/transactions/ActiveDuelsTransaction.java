package ru.job4j.db.transactions;

import ru.job4j.domain.duels.ActiveDuels;
import ru.job4j.domain.duels.AttackAction;

import java.util.Optional;

/**
 * Active duel transaction.
 *
 * The status of the user in the active duel means that in this request,
 * the duel page will be attacked or rendered.
 * Transaction begins with a method call inDuel(). If the user was not found
 * in active duels, then the transaction will be closed. Otherwise, the
 * transaction will be closed in the CloseConnection filter.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 07.04.2019
 */
public class ActiveDuelsTransaction implements ActiveDuels {
    private final Transaction transaction;
    private final ActiveDuels origin;

    public ActiveDuelsTransaction(final Transaction transaction,
                                  final ActiveDuels origin) {
        this.transaction = transaction;
        this.origin = origin;
    }

    /**
     * Start transaction for origin ActiveDuels.
     * @param userName user name.
     * @return result of origin ActiveDuels.
     */
    @Override
    public final Optional<AttackAction> inDuel(final String userName) {
        this.transaction.start();
        final Optional<AttackAction> result = this.origin.inDuel(userName);
        if (result.isEmpty()) {
            this.transaction.commit();
            this.transaction.finish();
        }
        return result;
    }
}
