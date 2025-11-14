package com.gasmapp.gasmapp.dto;

import com.gasmapp.gasmapp.model.ClientModel;

public class AuthResponse {

    private ClientModel client;
    private KeycloakTokenResponse token;

    public AuthResponse(ClientModel client, KeycloakTokenResponse token) {
        this.client = client;
        this.token = token;
    }

    public ClientModel getClient() {
        return client;
    }

    public KeycloakTokenResponse getToken() {
        return token;
    }
}
