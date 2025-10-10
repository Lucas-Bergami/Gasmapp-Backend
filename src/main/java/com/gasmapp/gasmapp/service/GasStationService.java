package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.GasStationModel;
import com.gasmapp.gasmapp.repository.GasStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GasStationService {

    @Autowired
    private GasStationRepository gasStationRepository;

    public GasStationModel createGasStation(GasStationModel gasStation) {
        return gasStationRepository.save(gasStation);
    }

    public Optional<GasStationModel> updateGasStation(Long id, GasStationModel updatedStation) {
        return gasStationRepository.findById(id).map(station -> {
            station.setName(updatedStation.getName());
            station.setLatitude(updatedStation.getLatitude());
            station.setLongitude(updatedStation.getLongitude());
            return gasStationRepository.save(station);
        });
    }

    public List<GasStationModel> getAllGasStations() {
        return gasStationRepository.findAll();
    }

    public Optional<GasStationModel> getGasStationById(Long id) {
        return gasStationRepository.findById(id);
    }

    public boolean deleteGasStation(Long id) {
        return gasStationRepository.findById(id).map(station -> {
            gasStationRepository.delete(station);
            return true;
        }).orElse(false);
    }

    public Optional<GasStationModel> getGasStationByName(String name) {
        return gasStationRepository.findByName(name);
    }

    public Optional<GasStationModel> getGasStationByCoordinates(Double latitude, Double longitude) {
        return gasStationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }
}
