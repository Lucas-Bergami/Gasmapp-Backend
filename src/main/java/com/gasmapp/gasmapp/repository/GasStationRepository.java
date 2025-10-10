package com.gasmapp.gasmapp.repository;

import com.gasmapp.gasmapp.model.GasStationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GasStationRepository extends JpaRepository<GasStationModel, Long> {
    Optional<GasStationModel> findByName(String name);
    Optional<GasStationModel> findByLatitudeAndLongitude(Double latitude, Double longitude);
}