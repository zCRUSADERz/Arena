package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.activity.ConstantLastActivity;
import ru.job4j.domain.duels.activity.DBLastActivity;
import ru.job4j.domain.duels.activity.LastActivityWrapper;
import ru.job4j.domain.duels.conditions.AttackConditionOnTimer;
import ru.job4j.domain.duels.duelists.ConstantDuelist;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.Duelist;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.TimeZone;
import java.util.function.Supplier;

public class SimpleDuelistFactory implements DuelistFactory, AutoCloseable {
    private final int turnDuration;
    private final Supplier<Connection> connectionFactory;

    public SimpleDuelistFactory(final int turnDuration,
                                final Supplier<Connection> connectionFactory) {
        this.turnDuration = turnDuration;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public final Duelist duelist(final String userName, final int damage,
                                 final int health, final Timestamp lastActivity) {
        return new ConstantDuelist(
                userName, damage, health,
                new AttackConditionOnTimer(
                        this.turnDuration,
                        () -> System.currentTimeMillis()
                                + TimeZone.getDefault().getRawOffset()
                ),
                new ConstantLastActivity(lastActivity)
        );
    }

    @Override
    public final DBDuelist duelist(final String userName,
                                   final int damage, final int health,
                                   final Timestamp lastActivity,
                                   final Timestamp now) {
        return new DBDuelist(
                userName,
                damage,
                health,
                new LastActivityWrapper(
                        new ConstantLastActivity(lastActivity),
                        new DBLastActivity(
                                userName,
                                this.connectionFactory
                        )
                ),
                new AttackConditionOnTimer(
                        this.turnDuration,
                        now::getTime
                ),
                this.connectionFactory
        );
    }

    @Override
    public final void close() throws Exception {
        this.connectionFactory.get().close();
    }
}
