package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.logs.AttackLog;

import java.sql.Connection;
import java.util.Collection;

public interface LogsFactory {

    Collection<AttackLog> logs(final int duelID);
}
