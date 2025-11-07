package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.EvaluationModel;
import com.gasmapp.gasmapp.model.FuelModel;
import com.gasmapp.gasmapp.model.GasStationModel;
import com.gasmapp.gasmapp.model.PriceModel;
import com.gasmapp.gasmapp.repository.ClientRepository;
import com.gasmapp.gasmapp.repository.EvaluationRepository;
import com.gasmapp.gasmapp.repository.GasStationRepository;
import com.gasmapp.gasmapp.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PriceRepository priceRepository;

    private final GasStationRepository gasStationRepository;

    public EvaluationService(GasStationRepository gasStationRepository,
                             EvaluationRepository evaluationRepository) {
        this.gasStationRepository = gasStationRepository;
        this.evaluationRepository = evaluationRepository;
    }

    public EvaluationModel createOrUpdateEvaluation(EvaluationModel evaluation) {
        var client = clientRepository.findById(evaluation.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        var price = priceRepository.findById(evaluation.getPrice().getId())
                .orElseThrow(() -> new RuntimeException("Price not found"));

        Optional<EvaluationModel> existingEvaluation =
                evaluationRepository.findByClientIdAndPriceId(client.getId(), price.getId());

        if (existingEvaluation.isPresent()) {
            EvaluationModel existing = existingEvaluation.get();
            existing.setEvaluation(evaluation.getEvaluation());
            return evaluationRepository.save(existing);
        } else {
            evaluation.setClient(client);
            evaluation.setPrice(price);
            return evaluationRepository.save(evaluation);
        }
    }

    public List<EvaluationModel> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<EvaluationModel> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    public boolean deleteEvaluation(Long id) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluationRepository.delete(evaluation);
            return true;
        }).orElse(false);
    }

    public Map<String, Map<String, Integer>> countLikesAndDislikesByGasStation(Long gasStationId) {
        GasStationModel gasStation = gasStationRepository.findById(gasStationId)
                .orElseThrow(() -> new RuntimeException("Gas station not found"));

        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();

        for (FuelModel fuel : gasStation.getFuels()) {
            List<PriceModel> prices = fuel.getPrices();
            if (prices.isEmpty()) continue;

            PriceModel lastPrice = prices.get(prices.size() - 1);

            List<EvaluationModel> evaluations = evaluationRepository.findByPriceId(lastPrice.getId());

            long likes = evaluations.stream()
                    .filter(e -> Boolean.TRUE.equals(e.getEvaluation()))
                    .count();

            long dislikes = evaluations.stream()
                    .filter(e -> Boolean.FALSE.equals(e.getEvaluation()))
                    .count();

            Map<String, Integer> stats = new HashMap<>();
            stats.put("likes", (int) likes);
            stats.put("dislikes", (int) dislikes);

            result.put(fuel.getName().name(), stats);
        }

        return result;
    }

    public Map<String, Long> countLikesDislikesByPrice(Long priceId) {
        var evaluations = evaluationRepository.findByPriceId(priceId);
        if (evaluations.isEmpty()) {
            return null; // ou new HashMap<>(); se preferir retornar vazio
        }

        long likes = evaluations.stream()
                .filter(e -> Boolean.TRUE.equals(e.getEvaluation()))
                .count();
        long dislikes = evaluations.stream()
                .filter(e -> Boolean.FALSE.equals(e.getEvaluation()))
                .count();

        Map<String, Long> result = new HashMap<>();
        result.put("likes", likes);
        result.put("dislikes", dislikes);

        return result;
    }
}
