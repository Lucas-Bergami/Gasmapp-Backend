package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.PriceModel;
import com.gasmapp.gasmapp.service.PriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<PriceModel> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceModel> getPriceById(@PathVariable Long id) {
        Optional<PriceModel> price = priceService.getPriceById(id);
        return price.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PriceModel> createPrice(@RequestBody PriceModel price) {
        PriceModel savedPrice = priceService.createPrice(price);
        return ResponseEntity.status(201).body(savedPrice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceModel> updatePrice(@PathVariable Long id, @RequestBody PriceModel updatedPrice) {
        Optional<PriceModel> price = priceService.updatePrice(id, updatedPrice);
        return price.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        boolean deleted = priceService.deletePrice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
