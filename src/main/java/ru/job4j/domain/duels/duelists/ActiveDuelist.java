package ru.job4j.domain.duels.duelists;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.results.AttackResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ActiveDuelist implements SimpleDuelist {
    private final String userName;
    private final ConnectionHolder connectionHolder;
    private final int turnDuration;

    public ActiveDuelist(final String userName,
                         final ConnectionHolder connectionHolder,
                         final int turnDuration) {
        this.userName = userName;
        this.connectionHolder = connectionHolder;
        this.turnDuration = turnDuration;
    }

    @Override
    public final String name() {
        return this.userName;
    }

    @Override
    public final Map<String, String> attributesFor(final String name) {
        final Map<String, String> result;
        final String query = ""
                + "SELECT damage, start_health, health "
                + "FROM active_duelists "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                result = new DuelistAttributes(
                        this.userName, resultSet
                ).attributesFor(name);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final int damage() {
        final int result;
        final String query = ""
                + "SELECT damage "
                + "FROM active_duelists "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("damage");
                } else {
                    throw new IllegalStateException(String.format(
                            "Active duelist: %s, not found.",
                            this.userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final int health() {
        final int result;
        final String query = ""
                + "SELECT health "
                + "FROM active_duelists "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("health");
                } else {
                    throw new IllegalStateException(String.format(
                            "Active duelist: %s, not found.",
                            this.userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final TurnTimer attackTimer() {
        final TurnTimer result;
        final String query = ""
                + "SELECT ad1.last_activity, ad2.last_activity, "
                + "CURRENT_TIMESTAMP(3) AS now "
                + "FROM active_duelists AS ad1 "
                + "JOIN active_duelists AS ad2 "
                + "ON ad1.user_name != ad2.user_name AND ad1.user_name = ? "
                + "AND ad1.duel_id = ad2.duel_id ";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final long userActivity = resultSet.getTimestamp(
                            "ad1.last_activity"
                    ).getTime();
                    final long opponentActivity = resultSet.getTimestamp(
                            "ad2.last_activity"
                    ).getTime();
                    final long now = resultSet.getTimestamp("now").getTime();
                    if (userActivity <= opponentActivity) {
                        result = new TurnTimer(true, 0);
                    } else {
                        final long timer = opponentActivity + this.turnDuration - now;
                        if (timer <= 0) {
                            result = new TurnTimer(true, 0);
                        } else {
                            result = new TurnTimer(
                                    false,
                                    (int) Math.ceil(((double) timer) / 1000)
                            );
                        }
                    }
                } else {
                    throw new IllegalStateException(String.format(
                            "Opponents for duelist: %s, not found.",
                            this.userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final AttackResult attack(final ActiveDuelist target) {
        if (!this.attackTimer().canAttack) {
            throw new IllegalStateException(String.format(
                    "User %s can not hit user %s, because it's not his turn now.",
                    this.userName, target.userName
            ));
        }
        final AttackResult result;
        final int newTargetHealth;
        final int userDamage = this.damage();
        final int targetHealth = target.health();
        final int resultHealth = targetHealth - userDamage;
        if (resultHealth <= 0) {
            newTargetHealth = 0;
            result = new AttackResult(
                    this.userName, target.userName, true, targetHealth
            );
        } else {
            newTargetHealth = resultHealth;
            result = new AttackResult(
                    this.userName, target.userName, false, userDamage
            );
        }
        target.update(0.0, newTargetHealth);
        /*
          The last activity of the attacker must be later attacked,
          in order to provide the subsequent move to the attacked.
         */
        this.update(0.01);
        return result;
    }

    public final void update(final double activityDelay, final int newHealth) {
        final String attackQuery = ""
                + "UPDATE active_duelists "
                + "SET health = ?, last_activity = CURRENT_TIMESTAMP(3) + ? "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(attackQuery)) {
            statement.setInt(1, newHealth);
            statement.setDouble(2, activityDelay);
            statement.setString(3, this.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error in update active duelist: %s",
                        this.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public final void update(final double activityDelay) {
        final String attackQuery = ""
                + "UPDATE active_duelists "
                + "SET last_activity = CURRENT_TIMESTAMP(3) + ? "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(attackQuery)) {
            statement.setDouble(1, activityDelay);
            statement.setString(2, this.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error in update active duelist: %s",
                        this.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static class TurnTimer {
        private final boolean canAttack;
        private final int timer;

        public TurnTimer(final boolean canAttack, final int timer) {
            this.canAttack = canAttack;
            this.timer = timer;
        }

        public final boolean canAttack() {
            return this.canAttack;
        }

        public final int timer() {
            return this.timer;
        }
    }
}
