package com.beeleza.stockflow.dto;

import com.beeleza.stockflow.entity.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponseDTO {
    private Long id;
    private MovementType type;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String reason;
    private LocalDateTime createdAt;
    private Long productId;
    private String productName;
}