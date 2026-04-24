package com.beeleza.stockflow.service;

import com.beeleza.stockflow.dto.CategoryRequestDTO;
import com.beeleza.stockflow.dto.CategoryResponseDTO;
import com.beeleza.stockflow.entity.Category;
import com.beeleza.stockflow.exception.ResourceNotFoundException;
import com.beeleza.stockflow.mapper.CategoryMapper;
import com.beeleza.stockflow.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryRequestDTO requestDTO;
    private CategoryResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Eletrônicos")
                .description("Produtos eletrônicos")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDTO = CategoryRequestDTO.builder()
                .name("Eletrônicos")
                .description("Produtos eletrônicos")
                .build();

        responseDTO = CategoryResponseDTO.builder()
                .id(1L)
                .name("Eletrônicos")
                .description("Produtos eletrônicos")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_shouldReturnListOfCategories() {
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTO(category)).thenReturn(responseDTO);

        List<CategoryResponseDTO> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnCategory_whenExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Eletrônicos", result.getName());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(999L));
    }

    @Test
    void save_shouldCreateCategory() {
        when(categoryMapper.toEntity(requestDTO)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.save(requestDTO);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void update_shouldUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toEntity(1L, requestDTO, category)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDTO(category)).thenReturn(responseDTO);

        CategoryResponseDTO result = categoryService.update(1L, requestDTO);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void update_shouldThrowException_whenNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.update(999L, requestDTO));
    }

    @Test
    void delete_shouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(999L));
    }
}