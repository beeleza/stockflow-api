package com.beeleza.stockflow.service;

import com.beeleza.stockflow.dto.ProductRequestDTO;
import com.beeleza.stockflow.dto.ProductResponseDTO;
import com.beeleza.stockflow.entity.Category;
import com.beeleza.stockflow.entity.Product;
import com.beeleza.stockflow.exception.ResourceNotFoundException;
import com.beeleza.stockflow.exception.ValidationException;
import com.beeleza.stockflow.mapper.ProductMapper;
import com.beeleza.stockflow.repository.CategoryRepository;
import com.beeleza.stockflow.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Category category;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Eletrônicos")
                .description("Produtos eletrônicos")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        product = Product.builder()
                .id(1L)
                .name("Notebook")
                .description("Notebook Dell")
                .sku("NOTE-DELL-001")
                .costPrice(new BigDecimal("2000.00"))
                .salePrice(new BigDecimal("2500.00"))
                .currentStock(10)
                .minStock(2)
                .maxStock(50)
                .reorderPoint(5)
                .position("A1")
                .status("ACTIVE")
                .category(category)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDTO = ProductRequestDTO.builder()
                .name("Notebook")
                .description("Notebook Dell")
                .sku("NOTE-DELL-001")
                .costPrice(new BigDecimal("2000.00"))
                .salePrice(new BigDecimal("2500.00"))
                .currentStock(10)
                .minStock(2)
                .maxStock(50)
                .reorderPoint(5)
                .position("A1")
                .status("ACTIVE")
                .categoryId(1L)
                .build();

        responseDTO = ProductResponseDTO.builder()
                .id(1L)
                .name("Notebook")
                .description("Notebook Dell")
                .sku("NOTE-DELL-001")
                .costPrice(new BigDecimal("2000.00"))
                .salePrice(new BigDecimal("2500.00"))
                .currentStock(10)
                .minStock(2)
                .maxStock(50)
                .reorderPoint(5)
                .position("A1")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll_shouldReturnPageOfProducts() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> productPage = new PageImpl<>(Arrays.asList(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        Page<ProductResponseDTO> result = productService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_shouldReturnEmptyPage_whenNoProducts() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Product> emptyPage = new PageImpl<>(Arrays.asList());
        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ProductResponseDTO> result = productService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(999L));
    }

    @Test
    void save_shouldCreateProduct() {
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(requestDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.save(requestDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void save_shouldThrowException_whenSkuAlreadyExists() {
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.of(product));

        assertThrows(ValidationException.class, () -> productService.save(requestDTO));
    }

    @Test
    void save_shouldThrowException_whenCategoryNotFound() {
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.save(requestDTO));
    }

    @Test
    void update_shouldUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(1L, requestDTO, product)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.update(1L, requestDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void update_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.update(999L, requestDTO));
    }

    @Test
    void update_shouldThrowException_whenSkuAlreadyExists() {
        Product otherProduct = Product.builder().id(2L).sku("NOTE-DELL-001").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.of(otherProduct));

        assertThrows(ValidationException.class, () -> productService.update(1L, requestDTO));
    }

    @Test
    void update_shouldThrowException_whenCategoryNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findBySku("NOTE-DELL-001")).thenReturn(Optional.empty());
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.update(1L, requestDTO));
    }

    @Test
    void delete_shouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(999L));
    }
}