package ru.job4j.domain.duels;

import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;

import java.sql.SQLException;

public class DBDuel {
    private final int duelId;
    private final DuelStartCondition startCondition;
    private final PairOfDuelist<DBDuelist> duelists;

    public DBDuel(final int duelId, final DuelStartCondition startCondition,
                  final PairOfDuelist<DBDuelist> duelists) {
        this.duelId = duelId;
        this.startCondition = startCondition;
        this.duelists = duelists;
    }

    public final void turn(final String userName) throws SQLException {
        if (!this.startCondition.started()) {
            throw new IllegalStateException(String.format(
                    "Duel: %s, not started. %s",
                    this.duelId, this.startCondition
            ));
        }
        final DBDuelist user = this.duelists.duelist(userName);
        final DBDuelist opponent = this.duelists.opponent(userName);
        user.attack(opponent);
    }
}
