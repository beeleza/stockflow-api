package com.beeleza.stockflow.dto;

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
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private Integer reorderPoint;
    private String position;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryResponseDTO category;
}