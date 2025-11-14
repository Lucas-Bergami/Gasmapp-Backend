package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.dto.AuthResponse;
import com.gasmapp.gasmapp.dto.GoogleTokenRequest;
import com.gasmapp.gasmapp.dto.GoogleUserInfo;
import com.gasmapp.gasmapp.dto.KeycloakTokenResponse;
import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import com.gasmapp.gasmapp.service.GoogleTokenValidator;
import com.gasmapp.gasmapp.service.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private GoogleTokenValidator googleTokenValidator;

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody GoogleTokenRequest request) {

        GoogleUserInfo googleInfo = googleTokenValidator.validate(request.getGoogleToken());

        KeycloakTokenResponse kcToken = keycloakService.exchangeGoogleToken(request.getGoogleToken());

        ClientModel client = clientRepository.findByEmail(googleInfo.getEmail())
                .orElseGet(() -> {
                    ClientModel newClient = new ClientModel();
                    newClient.setEmail(googleInfo.getEmail());
                    newClient.setName(googleInfo.getName());
                    newClient.setProviderId(googleInfo.getSub());
                    newClient.setKeycloakId(kcToken.getUserId());
                    return clientRepository.save(newClient);
                });

        return ResponseEntity.ok(new AuthResponse(client, kcToken));
    }
}