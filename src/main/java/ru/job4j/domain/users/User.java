package ru.job4j.domain.users;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.logs.results.AttackResult;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {
    private final String userName;
    private final ConnectionHolder connectionHolder;

    public User(final String userName, final ConnectionHolder connectionHolder) {
        this.userName = userName;
        this.connectionHolder = connectionHolder;
    }

    public final void upgrade(final AttackResult attackResult) {
        if (!attackResult.killed()) {
            throw new IllegalStateException(String.format(
                    "In this result: %s, no one killed anyone.",
                    attackResult
            ));
        }
        final int rating;
        if (this.userName.equals(attackResult.attacker())) {
            rating = 1;
        } else if (this.userName.equals(attackResult.target())) {
            rating = 2;
        } else {
            throw new IllegalStateException(String.format(
                    "User: %s, not found in attack result: %s",
                    this.userName, attackResult
            ));
        }
        final String upgrade = ""
                + "UPDATE users  "
                + "SET health = health + 1, damage = damage + 1, rating = rating + ? "
                + "WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(upgrade)) {
            statement.setInt(1, rating);
            statement.setString(2, this.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Upgrade error for user: %s.",
                        this.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
