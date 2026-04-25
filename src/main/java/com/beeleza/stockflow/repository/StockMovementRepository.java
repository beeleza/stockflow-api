package com.beeleza.stockflow.repository;

import com.beeleza.stockflow.entity.StockMovement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sm FROM StockMovement sm WHERE sm.product.id = :productId")
    List<StockMovement> findByProductIdWithLock(@Param("productId") Long productId);

    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);
}