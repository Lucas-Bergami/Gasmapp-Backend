package com.gasmapp.gasmapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasmapp.gasmapp.model.GasStationModel;
import com.gasmapp.gasmapp.repository.GasStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class GasStationService {

    @Autowired
    private GasStationRepository gasStationRepository;

    private final RestTemplate restTemplate = new RestTemplate();

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

    public void fetchGasStationsFromOverpass(double south, double west, double north, double east) {
        try {
            String query = String.format(Locale.US, """
    [out:json];
    node["amenity"="fuel"](%f,%f,%f,%f);
    out;
""", south, west, north, east);

            System.out.println("🔍 Enviando consulta para Overpass:");
            System.out.println(query);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);

            HttpEntity<String> request = new HttpEntity<>(query, headers);

            // Faz POST em vez de GET
            ResponseEntity<String> response = restTemplate.postForEntity("https://overpass-api.de/api/interpreter", request, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                System.out.println("⚠️ Falha ao consultar Overpass API: " + response.getStatusCode());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode elements = root.get("elements");

            if (elements == null || !elements.isArray() || elements.isEmpty()) {
                System.out.println("⚠️ Nenhum posto encontrado na área especificada.");
                return;
            }

            int count = 0;
            for (JsonNode element : elements) {
                String name = element.has("tags") && element.get("tags").has("name")
                        ? element.get("tags").get("name").asText()
                        : "Posto Desconhecido";

                double lat = element.get("lat").asDouble();
                double lon = element.get("lon").asDouble();

                Optional<GasStationModel> existing = gasStationRepository.findByLatitudeAndLongitude(lat, lon);
                if (existing.isPresent()) continue;

                GasStationModel station = new GasStationModel();
                station.setName(name);
                station.setLatitude(lat);
                station.setLongitude(lon);
                gasStationRepository.save(station);
                count++;
            }

            System.out.println("✅ " + count + " postos salvos no banco de dados!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
