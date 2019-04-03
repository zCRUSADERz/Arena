package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.DBDuel;
import ru.job4j.domain.duels.Duel;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.AttackLog;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Collection;

public interface DuelFactory {

    Duel duel(final Timestamp created, final Timestamp now,
              final PairOfDuelist<Duelist> duelists,
              final Collection<AttackLog> logs);

    DBDuel duel(final Connection connection, final int duelId,
                final Timestamp created, final Timestamp now,
                final PairOfDuelist<DBDuelist> duelists);
}
