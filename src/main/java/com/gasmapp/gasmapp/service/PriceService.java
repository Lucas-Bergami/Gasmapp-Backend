package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.PriceModel;
import com.gasmapp.gasmapp.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    @Autowired
    private PriceRepository priceRepository;

    public PriceModel createPrice(PriceModel price) {
        return priceRepository.save(price);
    }

    public Optional<PriceModel> updatePrice(Long id, PriceModel updatedPrice) {
        return priceRepository.findById(id).map(price -> {
            price.setPrice(updatedPrice.getPrice());
            return priceRepository.save(price);
        });
    }

    public List<PriceModel> getAllPrices() {
        return priceRepository.findAll();
    }

    public Optional<PriceModel> getPriceById(Long id) {
        return priceRepository.findById(id);
    }

    public boolean deletePrice(Long id) {
        return priceRepository.findById(id).map(price -> {
            priceRepository.delete(price);
            return true;
        }).orElse(false);
    }
}
