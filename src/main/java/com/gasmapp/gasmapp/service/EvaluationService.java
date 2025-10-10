package com.gasmapp.gasmapp.service;

import com.gasmapp.gasmapp.model.EvaluationModel;
import com.gasmapp.gasmapp.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    public EvaluationModel createEvaluation(EvaluationModel evaluation) {
        return evaluationRepository.save(evaluation);
    }

    public Optional<EvaluationModel> updateEvaluation(Long id, EvaluationModel updatedEvaluation) {
        return evaluationRepository.findById(id).map(evaluation -> {
            evaluation.setEvaluation(updatedEvaluation.getEvaluation());
            return evaluationRepository.save(evaluation);
        });
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
}
