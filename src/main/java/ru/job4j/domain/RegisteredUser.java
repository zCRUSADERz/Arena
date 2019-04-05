package ru.job4j.domain;

public class RegisteredUser implements User {
    private final String name;
    private final byte[] password;

    public RegisteredUser(final String name, final byte[] password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public final String name() {
        return this.name;
    }

    @Override
    public final byte[] password() {
        return this.password;
    }
}
