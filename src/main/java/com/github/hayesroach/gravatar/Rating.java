package com.github.hayesroach.gravatar;

public enum Rating {
    G(0),
    PG(1),
    R(2),
    X(3);

    public final int rating;

    private Rating(int rating) {
        this.rating = rating;
    }

    public int getNumberValue() {
        return rating;
    }
}
