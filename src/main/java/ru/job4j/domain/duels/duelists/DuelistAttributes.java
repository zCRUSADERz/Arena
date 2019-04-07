package ru.job4j.domain.duels.duelists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DuelistAttributes {
    private final String userName;
    private final ResultSet resultSet;

    public DuelistAttributes(final String userName, final ResultSet resultSet) {
        this.userName = userName;
        this.resultSet = resultSet;
    }

    public final Map<String, String> attributesFor(final String name) {
        final Map<String, String> result;
        try {
            if (resultSet.next()) {
                if (this.userName.equals(name)) {
                    result = Map.of(
                            "your_name", this.userName,
                            "your_damage", String.valueOf(
                                    resultSet.getInt("damage")
                            ),
                            "your_start_health", String.valueOf(
                                    resultSet.getInt("start_health")
                            ),
                            "your_health", String.valueOf(
                                    resultSet.getInt("health")
                            )
                    );
                } else {
                    result = Map.of(
                            "name", this.userName,
                            "damage", String.valueOf(
                                    resultSet.getInt("damage")
                            ),
                            "start_health", String.valueOf(
                                    resultSet.getInt("start_health")
                            ),
                            "health", String.valueOf(
                                    resultSet.getInt("health")
                            )
                    );
                }
            } else {
                throw new IllegalStateException(String.format(
                        "Duelist: %s, not found.",
                        this.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
