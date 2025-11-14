package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.ClientModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientModel createClient(ClientModel client) {
        return clientRepository.save(client);
    }

    public Optional<ClientModel> updateClient(Long id, ClientModel updatedClient) {
        return clientRepository.findById(id).map(client -> {
            client.setName(updatedClient.getName());
            client.setEmail(updatedClient.getEmail());
            client.setProviderId(updatedClient.getProviderId());
            client.setKeycloakId(updatedClient.getKeycloakId());
            return clientRepository.save(client);
        });
    }

    public List<ClientModel> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<ClientModel> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public boolean deleteClient(Long id) {
        return clientRepository.findById(id).map(client -> {
            clientRepository.delete(client);
            return true;
        }).orElse(false);
    }

    public Optional<ClientModel> getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
