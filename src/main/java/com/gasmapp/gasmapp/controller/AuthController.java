package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ClientModel request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        var client = (ClientModel) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("id", client.getId());
        response.put("name", client.getName());
        response.put("email", client.getEmail());

        return ResponseEntity.ok(response);
    }
}
