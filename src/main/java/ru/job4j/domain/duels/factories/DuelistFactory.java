package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.Duelist;

import java.sql.Timestamp;

public interface DuelistFactory {

    Duelist duelist(final String userName, final int damage,
                    final int health, final Timestamp lastActivity);

    DBDuelist duelist(final String userName,
                      final int damage, final int health,
                      final Timestamp lastActivity, final Timestamp now);
}
