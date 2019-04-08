package ru.job4j.domain.users.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Message digest factory.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 5.04.2019
 */
public class MessageDigestFactory {
    private final String passSalt;

    public MessageDigestFactory(final String passSalt) {
        this.passSalt = passSalt;
    }

    /**
     * Create MessageDigest based on SHA-512 algorithm and fixed salt.
     * @return MessageDigest.
     */
    public final MessageDigest messageDigest() {
        final MessageDigest result;
        try {
            result = MessageDigest.getInstance("SHA-512");
            result.update(this.passSalt.getBytes());
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
