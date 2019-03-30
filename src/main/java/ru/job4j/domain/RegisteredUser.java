package ru.job4j.domain;

public class RegisteredUser implements User {
    private final String name;
    private final String password;

    public RegisteredUser(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public final String name() {
        return this.name;
    }

    @Override
    public final String password() {
        return this.password;
    }
}
