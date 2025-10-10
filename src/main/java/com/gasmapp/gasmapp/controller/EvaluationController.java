package com.gasmapp.gasmapp.controller;

import com.gasmapp.gasmapp.model.EvaluationModel;
import com.gasmapp.gasmapp.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<EvaluationModel> evaluation = evaluationService.getEvaluationById(id);
        return evaluation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EvaluationModel> createEvaluation(@RequestBody EvaluationModel evaluation) {
        EvaluationModel savedEvaluation = evaluationService.createEvaluation(evaluation);
        return ResponseEntity.status(201).body(savedEvaluation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationModel> updateEvaluation(@PathVariable Long id, @RequestBody EvaluationModel updatedEvaluation) {
        Optional<EvaluationModel> evaluation = evaluationService.updateEvaluation(id, updatedEvaluation);
        return evaluation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        boolean deleted = evaluationService.deleteEvaluation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
