package ru.job4j.domain.duels.logs;

import ru.job4j.domain.duels.factories.LogsFactory;

import java.util.ArrayList;
import java.util.Collection;

public class GeneralDuelLog implements LogsFactory {
    private final AttackLogs attackLogs;
    private final FinalBlows finalBlows;

    public GeneralDuelLog(final AttackLogs attackLogs,
                          final FinalBlows finalBlows) {
        this.attackLogs = attackLogs;
        this.finalBlows = finalBlows;
    }

    @Override
    public final Collection<AttackLog> logs(final int duelID) {
        final Collection<AttackLog> result = new ArrayList<>(
                this.attackLogs.logs(duelID)
        );
        result.add(this.finalBlows.finalBlow(duelID));
        return result;
    }
}
