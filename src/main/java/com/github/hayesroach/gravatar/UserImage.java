package com.github.hayesroach.gravatar;

public class UserImage {

    public UserImage(String id, int rating, String url) {
        this.id = id;
        this.rating = rating;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    private String id;
}
