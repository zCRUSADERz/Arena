package ru.job4j.domain.duels.duelists;

import ru.job4j.domain.duels.activity.Activity;
import ru.job4j.domain.duels.conditions.AttackCondition;
import ru.job4j.domain.duels.logs.results.AttackResult;
import ru.job4j.domain.duels.logs.results.SimpleAttackResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDuelist implements SimpleDuelist {
    private final String userName;
    private final int damage;
    private final int health;
    private final Activity lastActivity;
    private final AttackCondition attackCondition;
    private final Connection connection;

    public DBDuelist(final String userName, final int damage, final int health,
                     final Activity lastActivity,
                     final AttackCondition attackCondition,
                     final Connection connection) {
        this.userName = userName;
        this.damage = damage;
        this.health = health;
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

    public final AttackResult attack(final DBDuelist target) {
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
        final int newTargetHealth;
        final AttackResult result;
        final int resultHealth = target.health - this.damage;
        if (resultHealth <= 0) {
            newTargetHealth = 0;
            result = new SimpleAttackResult(
                    this.userName, target.userName, true, target.health
            );
        } else {
            newTargetHealth = resultHealth;
            result = new SimpleAttackResult(
                    this.userName, target.userName, false, this.damage
            );
        }
        final String attackQuery = ""
                + "UPDATE active_duelists "
                + "SET health = ? "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connection.prepareStatement(attackQuery)) {
            statement.setInt(1, newTargetHealth);
            statement.setString(2, target.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error in setting the new value of health opponent: %s",
                        target.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
