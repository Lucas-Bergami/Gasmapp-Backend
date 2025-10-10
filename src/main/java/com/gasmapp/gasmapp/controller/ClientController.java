package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientModel> getClientById(@PathVariable Long id) {
        Optional<ClientModel> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClientModel createClient(@RequestBody ClientModel client) {
        return clientRepository.save(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientModel> updateClient(@PathVariable Long id, @RequestBody ClientModel updatedClient) {
        return clientRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setEmail(updatedClient.getEmail());
            client.setPassword(updatedClient.getPassword());
            clientRepository.save(client);
            return ResponseEntity.ok(client);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Optional<ClientModel> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            clientRepository.delete(clientOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
