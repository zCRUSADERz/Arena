package ru.job4j.domain.duels.activity;

import java.sql.SQLException;

public interface ActivityUpdateable {

    void update(final double delay) throws SQLException;
}
