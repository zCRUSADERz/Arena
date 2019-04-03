package ru.job4j.domain.duels.duelists;

import ru.job4j.domain.duels.activity.Activity;
import ru.job4j.domain.duels.conditions.AttackCondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDuelist implements SimpleDuelist {
    private final String userName;
    private final int damage;
    private final Activity lastActivity;
    private final AttackCondition attackCondition;
    private final Connection connection;

    public DBDuelist(final String userName, final int damage,
                     final Activity lastActivity,
                     final AttackCondition attackCondition,
                     final Connection connection) {
        this.userName = userName;
        this.damage = damage;
        this.lastActivity = lastActivity;
        this.attackCondition = attackCondition;
        this.connection = connection;
    }

    @Override
    public final String name() {
        return this.userName;
    }

    public final int damage() {
        return this.damage;
    }

    public final void attack(final DBDuelist target) throws SQLException {
        final String attackQuery = ""
                + "UPDATE users_in_duels  "
                + "SET health = "
                + "health - ? "
                + "WHERE user_name = ?";
        if (!this.attackCondition
                .canAttack(this.lastActivity, target.lastActivity)) {
            throw new IllegalStateException(String.format(
                    "User %s can not hit user %s, because it's not his turn now.",
                    this.userName, target.userName
            ));
        }
        target.lastActivity.update(0.0);
        /*
          The last activity of the attacker must be later attacked,
          in order to provide the subsequent move to the attacked.
         */
        this.lastActivity.update(0.01);
        try (final PreparedStatement statement
                     = this.connection.prepareStatement(attackQuery)) {
            statement.setInt(1, this.damage());
            statement.setString(2, target.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error in setting the new value of health opponent: %s",
                        target.userName
                ));
            }
        }
    }
}
