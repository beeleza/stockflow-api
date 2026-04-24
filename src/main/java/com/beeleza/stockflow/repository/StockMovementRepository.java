package com.beeleza.stockflow.repository;

import com.beeleza.stockflow.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
