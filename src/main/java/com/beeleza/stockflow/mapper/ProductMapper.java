package com.beeleza.stockflow.mapper;

import com.beeleza.stockflow.dto.CategoryResponseDTO;
import com.beeleza.stockflow.dto.ProductRequestDTO;
import com.beeleza.stockflow.dto.ProductResponseDTO;
import com.beeleza.stockflow.entity.Category;
import com.beeleza.stockflow.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .sku(dto.getSku())
                .costPrice(dto.getCostPrice())
                .salePrice(dto.getSalePrice())
                .currentStock(dto.getCurrentStock())
                .minStock(dto.getMinStock())
                .maxStock(dto.getMaxStock())
                .reorderPoint(dto.getReorderPoint())
                .position(dto.getPosition())
                .status(dto.getStatus())
                .build();
    }

    public ProductResponseDTO toDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .sku(product.getSku())
                .costPrice(product.getCostPrice())
                .salePrice(product.getSalePrice())
                .currentStock(product.getCurrentStock())
                .minStock(product.getMinStock())
                .maxStock(product.getMaxStock())
                .reorderPoint(product.getReorderPoint())
                .position(product.getPosition())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .category(product.getCategory() != null ? toCategoryDTO(product.getCategory()) : null)
                .build();
    }

    public Product toEntity(Long id, ProductRequestDTO dto, Product existing) {
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setSku(dto.getSku());
        existing.setCostPrice(dto.getCostPrice());
        existing.setSalePrice(dto.getSalePrice());
        existing.setCurrentStock(dto.getCurrentStock());
        existing.setMinStock(dto.getMinStock());
        existing.setMaxStock(dto.getMaxStock());
        existing.setReorderPoint(dto.getReorderPoint());
        existing.setPosition(dto.getPosition());
        existing.setStatus(dto.getStatus());
        return existing;
    }

    // metodo privado para mapear a categoria aninhada!
    private CategoryResponseDTO toCategoryDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}