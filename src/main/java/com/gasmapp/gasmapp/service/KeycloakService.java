package com.gasmapp.gasmapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasmapp.gasmapp.dto.KeycloakTokenResponse;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakService {

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public KeycloakTokenResponse exchangeGoogleToken(String googleToken) {

        String endpoint = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost post = new HttpPost(endpoint);

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange"));
            params.add(new BasicNameValuePair("subject_token", googleToken));
            params.add(new BasicNameValuePair("subject_token_type", "urn:ietf:params:oauth:token-type:jwt"));
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));

            post.setEntity(new UrlEncodedFormEntity(params));

            String json = client.execute(post, httpResponse ->
                    new String(httpResponse.getEntity().getContent().readAllBytes()));

            JsonNode obj = new ObjectMapper().readTree(json);

            return new KeycloakTokenResponse(
                    obj.get("access_token").asText(),
                    obj.get("refresh_token").asText(),
                    obj.get("sub").asText()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro no token exchange Keycloak: " + e.getMessage(), e);
        }
    }
}
