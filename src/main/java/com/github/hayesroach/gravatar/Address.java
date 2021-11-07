package com.github.hayesroach.gravatar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Address {
    private int rating;
    @JsonProperty(value = "userimage")
    private String userImage;
    @JsonProperty(value = "userimage_url")
    private String userImageUrl;
}
