package com.beeleza.stockflow.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotBlank(message = "SKU é obrigatório")
    private String sku;

    @NotNull(message = "Custo é obrigatório")
    @DecimalMin(value = "0.0", message = "Custo não pode ser negativo")
    private BigDecimal costPrice;

    @NotNull(message = "Preço de venda é obrigatório")
    @DecimalMin(value = "0.0", message = "Preço de venda não pode ser negativo")
    private BigDecimal salePrice;

    private Integer currentStock;

    private Integer minStock;

    private Integer maxStock;

    private Integer reorderPoint;

    private String position;

    @NotBlank(message = "Status é obrigatório")
    @Pattern(regexp = "ACTIVE|INACTIVE", message = "Status deve ser ACTIVE ou INACTIVE")
    private String status;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoryId;
}