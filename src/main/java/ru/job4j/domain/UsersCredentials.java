package ru.job4j.domain;

public class UsersCredentials {
    private final User first;
    private final User second;

    public UsersCredentials(final User first, final User second) {
        this.first = first;
        this.second = second;
    }

    public final boolean match() {
        return this.first.name().equals(this.second.name())
                && this.first.password().equals(this.second.password());
    }
}
