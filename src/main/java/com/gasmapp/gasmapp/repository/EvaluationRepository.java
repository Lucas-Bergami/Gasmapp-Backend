package com.gasmapp.gasmapp.repository;

import com.gasmapp.gasmapp.model.EvaluationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<EvaluationModel, Long> {
    Optional<EvaluationModel> findByClientIdAndPriceId(Long clientId, Long priceId);

    @Query("""
        SELECT COUNT(e) FROM EvaluationModel e
        WHERE e.evaluation = true
        AND e.price.fuel.gasStation.id = :gasStationId
    """)
    long countLikesByGasStation(Long gasStationId);

    @Query("""
        SELECT COUNT(e) FROM EvaluationModel e
        WHERE e.evaluation = false
        AND e.price.fuel.gasStation.id = :gasStationId
    """)
    long countDislikesByGasStation(Long gasStationId);

    List<EvaluationModel> findByPriceId(Long priceId);
}
