package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.GasStationModel;
import com.gasmapp.gasmapp.service.GasStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gasstations")
public class GasStationController {

    private final GasStationService gasStationService;

    public GasStationController(GasStationService gasStationService) {
        this.gasStationService = gasStationService;
    }

    @GetMapping
    public List<GasStationModel> getAllGasStations() {
        return gasStationService.getAllGasStations();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<GasStationModel>> getNearbyGasStations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "0.01") double delta) {

        double south = latitude - delta;
        double north = latitude + delta;
        double west = longitude - delta;
        double east = longitude + delta;

        List<GasStationModel> nearbyStations = gasStationService.getGasStationsInBoundingBox(south, west, north, east);

        if (nearbyStations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nearbyStations);
    }


    @GetMapping("/{id}")
    public ResponseEntity<GasStationModel> getGasStationById(@PathVariable Long id) {
        Optional<GasStationModel> station = gasStationService.getGasStationById(id);
        return station.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/coordinates")
    public ResponseEntity<GasStationModel> getGasStationByCoordinates(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {

        Optional<GasStationModel> station = gasStationService.getGasStationByCoordinates(latitude, longitude);
        return station.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GasStationModel> createGasStation(@RequestBody GasStationModel gasStation) {
        GasStationModel savedStation = gasStationService.createGasStation(gasStation);
        return ResponseEntity.status(201).body(savedStation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GasStationModel> updateGasStation(
            @PathVariable Long id,
            @RequestBody GasStationModel updatedStation) {

        Optional<GasStationModel> station = gasStationService.updateGasStation(id, updatedStation);
        return station.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGasStation(@PathVariable Long id) {
        boolean deleted = gasStationService.deleteGasStation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/details")
    public List<GasStationModel> getAllGasStationsWithDetails() {
        List<GasStationModel> stations = gasStationService.getAllGasStations();
        // .size garante que preços e combustíveis sejam carregados
        stations.forEach(station -> station.getFuels().forEach(fuel -> fuel.getPrices().size()));
        return stations;
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchGasStationsFromOverpass(
            @RequestParam double south,
            @RequestParam double west,
            @RequestParam double north,
            @RequestParam double east) {

        gasStationService.fetchGasStationsFromOverpass(south, west, north, east);
        return ResponseEntity.ok("Busca concluída e postos salvos no banco de dados!");
    }

    @PostMapping("/fetch-nearby")
    public ResponseEntity<String> fetchNearbyGasStations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "0.01") double delta) {


        double south = latitude - delta;
        double north = latitude + delta;
        double west = longitude - delta;
        double east = longitude + delta;

        gasStationService.fetchGasStationsFromOverpass(south, west, north, east);

        return ResponseEntity.ok("Requisição recebida! (" + south + "," + west + "," + north + "," + east + ")");
    }

}
