package ru.job4j.domain.duels;

import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.logs.AttackResult;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.AttackLogs;

import java.sql.SQLException;

public class DBDuel {
    private final int duelId;
    private final DuelStartCondition startCondition;
    private final PairOfDuelist<DBDuelist> duelists;
    private final AttackLogs attackLogs;

    public DBDuel(final int duelId, final DuelStartCondition startCondition,
                  final PairOfDuelist<DBDuelist> duelists,
                  final AttackLogs attackLogs) {
        this.duelId = duelId;
        this.startCondition = startCondition;
        this.duelists = duelists;
        this.attackLogs = attackLogs;
    }

    public final int id() {
        return this.duelId;
    }

    public final AttackResult turn(final String userName) throws SQLException {
        if (!this.startCondition.started()) {
            throw new IllegalStateException(String.format(
                    "Duel: %s, not started. %s",
                    this.duelId, this.startCondition
            ));
        }
        final DBDuelist user = this.duelists.duelist(userName);
        final DBDuelist opponent = this.duelists.opponent(userName);
        final AttackResult result = user.attack(opponent);
        if (!result.killed()) {
            this.attackLogs.create(
                    user.name(), this.duelId, opponent.name(), result.damage()
            );
        }
        return result;
    }
}
