package ru.job4j.domain.duels.duelists;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class DuelDuelists<T extends SimpleDuelist> {
    private final int duelId;
    private final ConnectionHolder connectionHolder;
    private final String query;
    private final Function<String, T> duelistsFactory;

    public DuelDuelists(final int duelId,
                        final ConnectionHolder connectionHolder,
                        final String query,
                        final Function<String, T> duelistsFactory) {
        this.duelId = duelId;
        this.connectionHolder = connectionHolder;
        this.query = query;
        this.duelistsFactory = duelistsFactory;
    }

    public final PairOfDuelist<T> duelists() {
        final PairOfDuelist<T> result;
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(this.query)) {
            statement.setInt(1, this.duelId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                final T first;
                final T second;
                if (resultSet.next()) {
                    first = this.duelistsFactory.apply(
                            resultSet.getString("ds.user_name")
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Duelist for duel: %s, not found.",
                            this.duelId
                    ));
                }
                if (resultSet.next()) {
                    second = this.duelistsFactory.apply(
                            resultSet.getString("ds.user_name")
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Duelist for duel: %s, not found.",
                            this.duelId
                    ));
                }
                result = new PairOfDuelist<>(first, second);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
