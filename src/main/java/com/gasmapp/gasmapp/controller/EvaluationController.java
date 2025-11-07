package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.EvaluationModel;
import com.gasmapp.gasmapp.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping
    public List<EvaluationModel> getAllEvaluations() {
        return evaluationService.getAllEvaluations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationModel> getEvaluationById(@PathVariable Long id) {
        return evaluationService.getEvaluationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EvaluationModel> createOrUpdateEvaluation(@RequestBody EvaluationModel evaluation) {
        EvaluationModel savedEvaluation = evaluationService.createOrUpdateEvaluation(evaluation);
        return ResponseEntity.ok(savedEvaluation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        boolean deleted = evaluationService.deleteEvaluation(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/trust-by-gasstation/{id}")
    public Map<String, Map<String, Integer>> getLikesAndDislikes(@PathVariable Long id) {
        return evaluationService.countLikesAndDislikesByGasStation(id);
    }

    @GetMapping("/trust-by-price/{priceId}")
    public ResponseEntity<?> countLikesDislikes(@PathVariable Long priceId) {
        var result = evaluationService.countLikesDislikesByPrice(priceId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
