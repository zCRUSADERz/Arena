package ru.job4j.domain.duels.duel;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.duelists.DuelDuelists;
import ru.job4j.domain.duels.duelists.FinishedDuelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.FinishedDuelLog;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

public class FinishedDuel {
    private final int duelID;
    private final ConnectionHolder connectionHolder;
    private final Function<String, FinishedDuelist> finishedDuelistFactory;
    private final IntFunction<FinishedDuelLog> duelLogFactory;

    public FinishedDuel(final int duelID, final ConnectionHolder connectionHolder,
                        final Function<String, FinishedDuelist> finishedDuelistFactory,
                        final IntFunction<FinishedDuelLog> duelLogFactory) {
        this.duelID = duelID;
        this.connectionHolder = connectionHolder;
        this.finishedDuelistFactory = finishedDuelistFactory;
        this.duelLogFactory = duelLogFactory;
    }

    public final DuelAttributes attributesFor(final String userName) {
        final Map<String, String> attr;
        final PairOfDuelist<FinishedDuelist> pair = this.duelists();
        attr = new HashMap<>(pair.attributesFor(userName));
        attr.put("finished", "true");
        return new DuelAttributes(
                attr,
                this.log().attributesFor(userName)
        );
    }

    public final PairOfDuelist<FinishedDuelist> duelists() {
        final String query = ""
                + "SELECT ds.user_name "
                + "FROM duels_history AS d "
                + "JOIN duelists_history AS ds "
                + "ON ds.duel_id = d.duel_id AND d.duel_id = ? ";
        return new DuelDuelists<>(
                this.duelID,
                this.connectionHolder,
                query,
                this.finishedDuelistFactory
        ).duelists();
    }

    public final FinishedDuelLog log() {
        return this.duelLogFactory.apply(this.duelID);
    }
}
