package com.gasmapp.gasmapp.repository;

import com.gasmapp.gasmapp.model.PriceModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<PriceModel, Long> {
}