package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.FuelModel;
import com.gasmapp.gasmapp.service.FuelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/fuels")
public class FuelController {

    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping
    public List<FuelModel> getAllFuels() {
        return fuelService.getAllFuels();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelModel> getFuelById(@PathVariable Long id) {
        Optional<FuelModel> fuel = fuelService.getFuelById(id);
        return fuel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<FuelModel> getFuelByName(@PathVariable String name) {
        Optional<FuelModel> fuel = fuelService.getFuelByNome(name);
        return fuel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FuelModel> createFuel(@RequestBody FuelModel fuel) {
        FuelModel savedFuel = fuelService.createFuel(fuel);
        return ResponseEntity.status(201).body(savedFuel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelModel> updateFuel(@PathVariable Long id, @RequestBody FuelModel updatedFuel) {
        Optional<FuelModel> fuel = fuelService.updateFuel(id, updatedFuel);
        return fuel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuel(@PathVariable Long id) {
        boolean deleted = fuelService.deleteFuel(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
