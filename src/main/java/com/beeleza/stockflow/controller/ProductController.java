package com.beeleza.stockflow.controller;

import com.beeleza.stockflow.dto.ProductRequestDTO;
import com.beeleza.stockflow.dto.ProductResponseDTO;
import com.beeleza.stockflow.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO created = productService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
