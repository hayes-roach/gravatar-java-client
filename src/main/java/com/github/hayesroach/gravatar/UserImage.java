package com.github.hayesroach.gravatar;

public class UserImage {

    public UserImage(int rating, String url) {
        this.rating = rating;
        this.url = url;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int rating;
    private String url;
}
