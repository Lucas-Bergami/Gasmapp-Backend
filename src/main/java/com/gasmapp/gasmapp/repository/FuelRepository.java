package com.gasmapp.gasmapp.repository;

import com.gasmapp.gasmapp.model.FuelModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuelRepository extends JpaRepository<FuelModel, Long> {
    Optional<FuelModel> findByName(String nome);
}