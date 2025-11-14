package com.gasmapp.gasmapp.dto;

public class KeycloakTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;

    public KeycloakTokenResponse(String accessToken, String refreshToken, String userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserId() {
        return userId;
    }
}
