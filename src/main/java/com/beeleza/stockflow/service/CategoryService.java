package com.beeleza.stockflow.service;

import com.beeleza.stockflow.dto.CategoryRequestDTO;
import com.beeleza.stockflow.dto.CategoryResponseDTO;
import com.beeleza.stockflow.entity.Category;
import com.beeleza.stockflow.exception.ResourceNotFoundException;
import com.beeleza.stockflow.mapper.CategoryMapper;
import com.beeleza.stockflow.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));
        return categoryMapper.toDTO(category);
    }

    @Transactional
    public CategoryResponseDTO save(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));
        categoryMapper.toEntity(id, dto, existing);
        existing.setUpdatedAt(LocalDateTime.now());
        Category updated = categoryRepository.save(existing);
        return categoryMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + id));
        categoryRepository.delete(category);
    }
}