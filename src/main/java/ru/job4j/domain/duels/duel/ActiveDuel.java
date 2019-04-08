package ru.job4j.domain.duels.duel;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.duelists.ActiveDuelist;
import ru.job4j.domain.duels.duelists.DuelDuelists;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.ActiveDuelLog;
import ru.job4j.domain.duels.results.DuelAttackResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Active duel.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 6.04.2019
 */
public class ActiveDuel {
    private final int duelId;
    private final ConnectionHolder connectionHolder;
    private final int startDelay;
    private final Function<String, ActiveDuelist> activeDuelistFactory;

    public ActiveDuel(final int duelId, final ConnectionHolder connectionHolder,
                      final int startDelay,
                      final Function<String, ActiveDuelist> activeDuelistFactory) {
        this.duelId = duelId;
        this.connectionHolder = connectionHolder;
        this.startDelay = startDelay;
        this.activeDuelistFactory = activeDuelistFactory;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * Includes: duel startTimer, user turnTimer, duelists attributes, logs.
     * @param userName prepares for user.
     * @return DuelAttributes.
     */
    public final DuelAttributes attributesFor(final String userName) {
        final Map<String, String> attr = new HashMap<>();
        final PairOfDuelist<ActiveDuelist> pair = this.duelists();
        final ActiveDuelist user = pair.duelist(userName);
        if (this.started()) {
            final ActiveDuelist.TurnTimer turnTimer = user.attackTimer();
            if (!turnTimer.canAttack()) {
                attr.put("turnTimer", String.valueOf(turnTimer.timer()));
            }
        } else {
            attr.put("startTimer", String.valueOf(this.startTimer()));
        }
        attr.putAll(pair.attributesFor(userName));
        return new DuelAttributes(
                attr,
                new ActiveDuelLog(
                        this.duelId, this.connectionHolder
                ).attributesFor(userName)
        );
    }

    /**
     * @return a couple of duelists for this duel.
     */
    public final PairOfDuelist<ActiveDuelist> duelists() {
        final String query = ""
                + "SELECT ds.user_name "
                + "FROM active_duels AS d "
                + "JOIN active_duelists AS ds "
                + "ON ds.duel_id = d.duel_id AND d.duel_id = ? ";
        return new DuelDuelists<>(
                this.duelId,
                this.connectionHolder,
                query,
                this.activeDuelistFactory
        ).duelists();
    }

    /**
     * @return true, if the delay time of the start for this duel has passed.
     */
    public final boolean started() {
        return this.startTimer() <= 0;
    }

    /**
     * @return the time remaining before the start of this duel. In seconds.
     */
    public final long startTimer() {
        final int result;
        final String query = ""
                + "SELECT CURRENT_TIMESTAMP - created AS time_passed "
                + "FROM active_duels WHERE duel_id = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setInt(1, this.duelId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final long timer = this.startDelay - resultSet.getLong("time_passed");
                    if (timer < 0) {
                        result = 0;
                    } else {
                        result = (int) timer;
                    }
                } else {
                    throw new IllegalStateException(String.format(
                            "Active duel with id: %d, not found.",
                            this.duelId
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    /**
     * Performs move for this user.
     * @param userName user name.
     * @return duel attack result.
     */
    public final DuelAttackResult turn(final String userName) {
        if (!this.started()) {
            throw new IllegalStateException(String.format(
                    "Duel: %s, not started",
                    this.duelId
            ));
        }
        final PairOfDuelist<ActiveDuelist> pair = this.duelists();
        final ActiveDuelist user = pair.duelist(userName);
        final ActiveDuelist opponent = pair.opponent(userName);
        final DuelAttackResult attackResult = new DuelAttackResult(
                this.duelId,
                user.attack(opponent)
        );
        if (!attackResult.killed()) {
            new ActiveDuelLog(
                    this.duelId, this.connectionHolder
            ).create(attackResult);
        }
        return attackResult;
    }

    /**
     * Add a couple of duelists to this duel.
     * In a duel there can be only two duelists.
     * @param first duelist.
     * @param second duelist.
     */
    public final void addDuelers(final String first, final String second) {
        final Consumer<String> addDueler =  userName -> {
            final String query = ""
                    + "INSERT INTO active_duelists "
                    + "(user_name, duel_id, start_health, health, damage) "
                    + "SELECT name, ? AS duel_id, health AS start_health, health, damage "
                    + "FROM users WHERE name = ?";
            try (final PreparedStatement statement
                         = this.connectionHolder.connection().prepareStatement(query)) {
                statement.setInt(1, duelId);
                statement.setString(2, userName);
                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(String.format(
                            "failed to attach the user to the duel. User name: %s",
                            userName
                    ));
                }
            } catch (final SQLException ex) {
                throw new IllegalStateException(ex);
            }
        };
        addDueler.accept(first);
        addDueler.accept(second);
    }
}
