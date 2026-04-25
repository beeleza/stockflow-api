package com.beeleza.stockflow.service;

import com.beeleza.stockflow.dto.StockMovementRequestDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private StockMovementRequestDTO requestDTO;

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

        requestDTO = StockMovementRequestDTO.builder()
                .productId(1L)
                .quantity(5)
                .unitPrice(new BigDecimal("100.00"))
                .reason("Compra")
                .build();
    }

    @Test
    void registerEntry_shouldIncreaseStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(requestDTO);

        assertEquals(15, activeProduct.getCurrentStock());
        verify(stockMovementRepository, times(1)).save(any(StockMovement.class));
    }

    @Test
    void registerExit_shouldDecreaseStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        requestDTO.setQuantity(3);
        stockMovementService.registerExit(requestDTO);

        assertEquals(7, activeProduct.getCurrentStock());
    }

    @Test
    void registerAdjustment_shouldAdjustStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        requestDTO.setQuantity(2);
        stockMovementService.registerAdjustment(requestDTO);

        assertEquals(12, activeProduct.getCurrentStock());
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsNull() {
        requestDTO.setQuantity(null);

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsZero() {
        requestDTO.setQuantity(0);

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerEntry_shouldThrowException_whenQuantityIsNegative() {
        requestDTO.setQuantity(-5);

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductIsNull() {
        requestDTO.setProductId(null);

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductNotFound() {
        requestDTO.setProductId(999L);
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerEntry_shouldThrowException_whenProductIsInactive() {
        activeProduct.setStatus("INACTIVE");
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));

        assertThrows(ValidationException.class, () ->
                stockMovementService.registerEntry(requestDTO));
    }

    @Test
    void registerExit_shouldThrowException_whenStockWouldBeNegative() {
        activeProduct.setCurrentStock(2);
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());

        requestDTO.setQuantity(5);
        assertThrows(ValidationException.class, () ->
                stockMovementService.registerExit(requestDTO));
    }

    @Test
    void registerEntry_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(requestDTO);

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.IN));
    }

    @Test
    void registerExit_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerExit(requestDTO);

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.OUT));
    }

    @Test
    void registerAdjustment_shouldCreateMovementWithCorrectType() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerAdjustment(requestDTO);

        verify(stockMovementRepository).save(argThat(movement ->
                movement.getType() == MovementType.ADJUST));
    }

    @Test
    void registerEntry_shouldHandleNullCurrentStock() {
        activeProduct.setCurrentStock(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(activeProduct));
        when(stockMovementRepository.findByProductIdWithLock(1L)).thenReturn(Arrays.asList());
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenReturn(activeProduct);

        stockMovementService.registerEntry(requestDTO);

        assertEquals(5, activeProduct.getCurrentStock());
    }

    @Test
    void findByProductId_shouldReturnMovements() {
        StockMovement movement = StockMovement.builder()
                .id(1L)
                .type(MovementType.IN)
                .quantity(5)
                .product(activeProduct)
                .build();
        when(productRepository.existsById(1L)).thenReturn(true);
        when(stockMovementRepository.findByProductIdOrderByCreatedAtDesc(1L)).thenReturn(Arrays.asList(movement));

        List<StockMovement> result = stockMovementService.findByProductId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findByProductId_shouldThrowException_whenProductIdIsNull() {
        assertThrows(ValidationException.class, () ->
                stockMovementService.findByProductId(null));
    }

    @Test
    void findByProductId_shouldThrowException_whenProductNotFound() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                stockMovementService.findByProductId(999L));
    }
}