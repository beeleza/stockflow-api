package com.beeleza.stockflow.controller;

import com.beeleza.stockflow.dto.CategoryRequestDTO;
import com.beeleza.stockflow.dto.CategoryResponseDTO;
import com.beeleza.stockflow.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "Categories", description = "Gerenciamento de categorias")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Listar todas as categorias")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Buscar categoria por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(summary = "Criar nova categoria")
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) {
        CategoryResponseDTO created = categoryService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualizar categoria")
    @PutMapping("{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDTO dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }

    @Operation(summary = "Excluir categoria")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}