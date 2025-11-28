package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.model.EvaluationModel;
import com.gasmapp.gasmapp.model.PriceModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientModel> getClientById(@PathVariable Long id) {
        Optional<ClientModel> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientModel client) {

        if (client.getName() == null || client.getName().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Name is required");
        }

        if (client.getEmail() == null || client.getEmail().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email is required");
        }

        if (client.getPassword() == null || client.getPassword().isBlank()) {
            return ResponseEntity
                    .badRequest()
                    .body("Password is required");
        }

        if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Client with this email already exists");
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setRole("CLIENT");

        ClientModel savedClient = clientRepository.save(client);

        return ResponseEntity.status(201).body(savedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientModel> updateClient(@PathVariable Long id, @RequestBody ClientModel updatedClient) {
        return clientRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setEmail(updatedClient.getEmail());
            client.setPassword(updatedClient.getPassword());
            ClientModel saved = clientRepository.save(client);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Optional<ClientModel> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) { clientRepository.delete(clientOpt.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }
    
    @GetMapping("/{id}/evaluations")
    public ResponseEntity<List<EvaluationModel>> getEvaluationsByClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> ResponseEntity.ok(client.getEvaluations()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/prices")
    public ResponseEntity<List<PriceModel>> getPricesByClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(client -> ResponseEntity.ok(client.getPrices()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
