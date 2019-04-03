package ru.job4j.domain.duels.activity;

import java.sql.Timestamp;

public class ConstantLastActivity implements LastActivity {
    /**
     * Date of last player activity in seconds
     */
    private final long lastActivity;

    public ConstantLastActivity(final Timestamp lastActivity) {
        this(lastActivity.getTime());
    }

    public ConstantLastActivity(final long lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    public final long activity() {
        return this.lastActivity;
    }
}
