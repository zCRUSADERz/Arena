package ru.job4j.db;

import com.zaxxer.hikari.HikariConfig;

public class DBConfig {
    private final String username;
    private final String password;

    public DBConfig() {
        this("root", "password123A");
    }

    public DBConfig(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public final HikariConfig config() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(
                "jdbc:mysql://localhost:3306/Arena"
                        + "?useUnicode=true&useJDBCCompliantTimezoneShift=true"
                        + "&useLegacyDatetimeCode=false&serverTimezone=UTC"
        );
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setMaximumPoolSize(10);
        config.setAutoCommit(true);
        return config;
    }
}
