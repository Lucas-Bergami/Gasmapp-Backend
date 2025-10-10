package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.FuelModel;
import com.gasmapp.gasmapp.repository.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuelService {

    @Autowired
    private FuelRepository fuelRepository;

    public FuelModel createFuel(FuelModel fuel) {
        return fuelRepository.save(fuel);
    }

    public Optional<FuelModel> updateFuel(Long id, FuelModel updatedFuel) {
        return fuelRepository.findById(id).map(fuel -> {
            fuel.setName(updatedFuel.getName());
            return fuelRepository.save(fuel);
        });
    }

    public List<FuelModel> getAllFuels() {
        return fuelRepository.findAll();
    }

    public Optional<FuelModel> getFuelById(Long id) {
        return fuelRepository.findById(id);
    }

    public boolean deleteFuel(Long id) {
        return fuelRepository.findById(id).map(fuel -> {
            fuelRepository.delete(fuel);
            return true;
        }).orElse(false);
    }

    public Optional<FuelModel> getFuelByNome(String nome) {
        return fuelRepository.findByName(nome);
    }
}
