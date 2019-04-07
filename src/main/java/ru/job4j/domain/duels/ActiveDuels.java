package ru.job4j.domain.duels;

import java.util.Optional;

public interface ActiveDuels {

    Optional<AttackAction> inDuel(final String userName);
}
