package ru.job4j.domain.duels.activity;

import java.sql.SQLException;

public class LastActivityWrapper implements Activity {
    private final LastActivity activity;
    private final ActivityUpdateable updateableActivity;

    public LastActivityWrapper(final LastActivity activity,
                               final ActivityUpdateable updateableActivity) {
        this.activity = activity;
        this.updateableActivity = updateableActivity;
    }

    @Override
    public final long activity() {
        return this.activity.activity();
    }

    @Override
    public final void update(final double delay) throws SQLException {
        this.updateableActivity.update(delay);
    }
}
