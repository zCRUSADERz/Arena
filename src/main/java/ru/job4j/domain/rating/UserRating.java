package ru.job4j.domain.rating;

public class UserRating {
    private final int rating;
    private final int victories;
    private final int defeat;

    public UserRating(final int rating, final int victories, final int defeat) {
        this.rating = rating;
        this.victories = victories;
        this.defeat = defeat;
    }

    public final int rating() {
        return this.rating;
    }

    public final int victories() {
        return this.victories;
    }

    public final int defeat() {
        return this.defeat;
    }
}
