package com.github.hayesroach.gravatar;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public Address() {
    }

    public Address(int rating, String userImage, String userImageUrl) {
        this.rating = rating;
        this.userImage = userImage;
        this.userImageUrl = userImageUrl;
    }

    private int rating;
    @JsonProperty(value = "userimage")
    private String userImage;
    @JsonProperty(value = "userimage_url")
    private String userImageUrl;
}
