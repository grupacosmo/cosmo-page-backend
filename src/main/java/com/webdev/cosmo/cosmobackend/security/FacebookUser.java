package com.webdev.cosmo.cosmobackend.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacebookUser {

    public FacebookUser(String accessToken, String userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String userId;

}
