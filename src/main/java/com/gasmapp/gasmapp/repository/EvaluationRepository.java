package com.gasmapp.gasmapp.repository;

import com.gasmapp.gasmapp.model.EvaluationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<EvaluationModel, Long> {
}
