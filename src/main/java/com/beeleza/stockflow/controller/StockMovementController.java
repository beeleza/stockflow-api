package com.beeleza.stockflow.controller;

import com.beeleza.stockflow.dto.StockMovementRequestDTO;
import com.beeleza.stockflow.dto.StockMovementResponseDTO;
import com.beeleza.stockflow.service.StockMovementService;
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
@RequestMapping("/stock-movement")
@Tag(name = "Stock Movements", description = "Gerenciamento de movimentações de estoque")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    @Operation(summary = "Registrar entrada de estoque")
    @PostMapping("/entry")
    public ResponseEntity<Void> registerEntry(
            @RequestBody @Valid StockMovementRequestDTO dto
    ) {
        stockMovementService.registerEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Registrar saída de estoque")
    @PostMapping("/exit")
    public ResponseEntity<Void> registerExit(
            @RequestBody @Valid StockMovementRequestDTO dto
    ) {
        stockMovementService.registerExit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Registrar ajuste de estoque")
    @PostMapping("/adjustment")
    public ResponseEntity<Void> registerAdjustment(
            @RequestBody @Valid StockMovementRequestDTO dto
    ) {
        stockMovementService.registerAdjustment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Listar movimentações por produto")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockMovementResponseDTO>> findByProductId(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(stockMovementService.findByProductId(productId));
    }

    @Operation(summary = "Buscar movimentação por ID")
    @GetMapping("/{id}")
    public ResponseEntity<StockMovementResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(stockMovementService.findById(id));
    }
}