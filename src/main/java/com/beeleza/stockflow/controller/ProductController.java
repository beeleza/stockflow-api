package com.beeleza.stockflow.controller;

import com.beeleza.stockflow.dto.ProductRequestDTO;
import com.beeleza.stockflow.dto.ProductResponseDTO;
import com.beeleza.stockflow.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Products", description = "Gerenciamento de produtos")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma página de produtos")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(
            @Parameter(hidden = true) @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "Criar novo produto")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO created = productService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar produto")
    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @Operation(summary = "Excluir produto")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
