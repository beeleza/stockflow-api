package com.beeleza.stockflow.service;

import com.beeleza.stockflow.entity.Product;
import com.beeleza.stockflow.entity.StockMovement;
import com.beeleza.stockflow.entity.enums.MovementType;
import com.beeleza.stockflow.exception.ResourceNotFoundException;
import com.beeleza.stockflow.exception.ValidationException;
import com.beeleza.stockflow.repository.ProductRepository;
import com.beeleza.stockflow.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockMovementService stockMovementService;

    private Product activeProduct;

    @BeforeEach
    void setUp() {
        activeProduct = Product.builder()
                .id(1L)
                .name("Notebook")
                .sku("NOTE-001")
                .status("ACTIVE")
                .currentStock(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void registerEntry_shouldIncreaseStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(1L, 5, new BigDecimal("100.00"), "Compra");

        assertEquals(15, activeProduct.getCurrentStock());
        verify(stockMovementRepository, times(1)).save(any(StockMovement.class));
    }

    @Test
    void registerExit_shouldDecreaseStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerExit(1L, 3, new BigDecimal("100.00"), "Venda");

        assertEquals(7, activeProduct.getCurrentStock());
    }

    @Test
    void registerAdjustment_shouldAdjustStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerAdjustment(1L, -2, "Ajuste de inventario");

        assertEquals(8, activeProduct.getCurrentStock());
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsNull() {
        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(1L, null, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsZero() {
        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(1L, 0, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsNegative() {
        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(1L, -5, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductIsNull() {
        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(null, 5, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                stockMovementService.registerEntry(999L, 5, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductIsInactive() {
        activeProduct.setStatus("INACTIVE");
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(1L, 5, new BigDecimal("100.00"), "Compra"));
    }

    @Test
    void registerExit_shouldThrowException_whenStockWouldBeNegative() {
        activeProduct.setCurrentStock(2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerExit(1L, 5, new BigDecimal("100.00"), "Venda"));
    }

    @Test
    void registerAdjustment_shouldThrowException_whenStockWouldBeNegative() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerAdjustment(1L, -100, "Ajuste"));
    }

    @Test
    void registerEntry_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(1L, 5, new BigDecimal("100.00"), "Compra");

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.IN));
    }

    @Test
    void registerExit_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerExit(1L, 3, new BigDecimal("100.00"), "Venda");

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.OUT));
    }

    @Test
    void registerAdjustment_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerAdjustment(1L, 2, "Auditoria");

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.ADJUST));
    }

    @Test
    void registerEntry_shouldHandleNullCurrentStock() {
        activeProduct.setCurrentStock(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(1L, 5, new BigDecimal("100.00"), "Compra");

        assertEquals(5, activeProduct.getCurrentStock());
    }
}