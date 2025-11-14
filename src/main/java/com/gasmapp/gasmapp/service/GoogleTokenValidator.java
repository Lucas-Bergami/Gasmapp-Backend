package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.dto.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenValidator {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenValidator() {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new JacksonFactory()
        )
                .setAudience(Collections.singletonList("SEU_GOOGLE_CLIENT_ID"))
                .build();
    }

    public GoogleUserInfo validate(String token) {
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                throw new IllegalArgumentException("Token Google inválido");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return new GoogleUserInfo(
                    payload.getEmail(),
                    (String) payload.get("name"),
                    payload.getSubject() // "sub" do Google
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro validando token Google: " + e.getMessage(), e);
        }
    }
}
