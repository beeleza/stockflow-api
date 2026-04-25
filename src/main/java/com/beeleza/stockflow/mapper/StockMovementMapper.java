package com.beeleza.stockflow.mapper;

import com.beeleza.stockflow.dto.StockMovementResponseDTO;
import com.beeleza.stockflow.entity.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovementResponseDTO toDTO(StockMovement movement) {
        return StockMovementResponseDTO.builder()
                .id(movement.getId())
                .type(movement.getType())
                .quantity(movement.getQuantity())
                .unitPrice(movement.getUnitPrice())
                .reason(movement.getReason())
                .createdAt(movement.getCreatedAt())
                .productId(movement.getProduct() != null ? movement.getProduct().getId() : null)
                .productName(movement.getProduct() != null ? movement.getProduct().getName() : null)
                .build();
    }
}