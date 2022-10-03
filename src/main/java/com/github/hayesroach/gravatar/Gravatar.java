package com.github.hayesroach.gravatar;

import java.util.Objects;

public class Gravatar {

    public Gravatar(String id, Rating rating, String url) {
        this.id = id;
        this.rating = rating;
        this.url = url;
    }

    public Gravatar(String id, Rating rating) {
        this.id = id;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserImage{" +
                "rating=" + rating +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gravatar)) return false;
        Gravatar gravatar = (Gravatar) o;
        return rating == gravatar.rating &&
                Objects.equals(url, gravatar.url) &&
                Objects.equals(id, gravatar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, url, id);
    }

    private Rating rating;
    private String url;
    private String id;
}
