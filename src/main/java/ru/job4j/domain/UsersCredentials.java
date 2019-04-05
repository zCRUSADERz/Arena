package ru.job4j.domain;

import java.util.Arrays;

public class UsersCredentials {
    private final User first;
    private final User second;

    public UsersCredentials(final User first, final User second) {
        this.first = first;
        this.second = second;
    }

    public final boolean match() {
        return this.first.name().equals(this.second.name())
                && Arrays.equals(this.first.password(), this.second.password());
    }
}
