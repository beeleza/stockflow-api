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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        return productMapper.toDTO(product);
    }

    @Transactional
    public ProductResponseDTO save(ProductRequestDTO dto) {
        productRepository.findBySku(dto.getSku())
                .ifPresent(p -> {
                    throw new ValidationException("SKU já existe: " + dto.getSku());
                });

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + dto.getCategoryId()));

        Product product = productMapper.toEntity(dto);
        product.setCategory(category);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        productRepository.findBySku(dto.getSku())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> {
                    throw new ValidationException("SKU já existe: " + dto.getSku());
                });

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + dto.getCategoryId()));

        productMapper.toEntity(id, dto, existing);
        existing.setCategory(category);
        existing.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepository.save(existing);
        return productMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        productRepository.delete(product);
    }
}
