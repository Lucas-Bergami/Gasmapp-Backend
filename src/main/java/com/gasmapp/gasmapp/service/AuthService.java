package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import com.gasmapp.gasmapp.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClientRepository clientRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(ClientRepository clientRepository, JwtUtil jwtUtil) {
        this.clientRepository = clientRepository;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {

        ClientModel client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!encoder.matches(password, client.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return jwtUtil.generateToken(client.getEmail());
    }
}
