package com.beeleza.stockflow.mapper;

import com.beeleza.stockflow.dto.CategoryRequestDTO;
import com.beeleza.stockflow.dto.CategoryResponseDTO;
import com.beeleza.stockflow.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public CategoryResponseDTO toDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public Category toEntity(Long id, CategoryRequestDTO dto, Category existing) {
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return existing;
    }
}