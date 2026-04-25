package com.beeleza.stockflow.service;

import com.beeleza.stockflow.entity.Product;
import com.beeleza.stockflow.entity.StockMovement;
import com.beeleza.stockflow.entity.enums.MovementType;
import com.beeleza.stockflow.exception.ResourceNotFoundException;
import com.beeleza.stockflow.exception.ValidationException;
import com.beeleza.stockflow.repository.ProductRepository;
import com.beeleza.stockflow.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    private static final String STATUS_ACTIVE = "ACTIVE";

    @Transactional
    public void registerEntry(Long productId, Integer quantity, BigDecimal unitPrice, String reason) {
        registerMovement(productId, quantity, unitPrice, reason, MovementType.IN);
    }

    @Transactional
    public void registerExit(Long productId, Integer quantity, BigDecimal unitPrice, String reason) {
        registerMovement(productId, quantity, unitPrice, reason, MovementType.OUT);
    }

    @Transactional
    public void registerAdjustment(Long productId, Integer quantity, String reason) {
        registerMovement(productId, quantity, null, reason, MovementType.ADJUST);
    }

    private void registerMovement(Long productId, Integer quantity, BigDecimal unitPrice, String reason, MovementType type) {
        if (type == MovementType.ADJUST) {
            validateAdjustmentQuantity(quantity);
        } else {
            validateQuantity(quantity);
        }

        Product product = validateAndGetProduct(productId);

        StockMovement movement = StockMovement.builder()
                .type(type)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .reason(reason)
                .product(product)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        stockMovementRepository.save(movement);

        updateStock(product, quantity, type);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ValidationException("Quantidade deve ser maior que zero");
        }
    }

    private void validateAdjustmentQuantity(Integer quantity) {
        if (quantity == null || quantity == 0) {
            throw new ValidationException("Quantidade de ajuste não pode ser zero");
        }
    }

    private Product validateAndGetProduct(Long productId) {
        if (productId == null) {
            throw new ValidationException("Produto é obrigatório");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + productId));

        if (!STATUS_ACTIVE.equals(product.getStatus())) {
            throw new ValidationException("Produto deve estar ACTIVE para movimentação de estoque");
        }

        return product;
    }

    private void updateStock(Product product, Integer quantity, MovementType type) {
        int currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
        int newStock;

        switch (type) {
            case IN:
                newStock = currentStock + quantity;
                break;
            case OUT:
                newStock = currentStock - quantity;
                if (newStock < 0) {
                    throw new ValidationException("Estoque negativo não permitido");
                }
                break;
            case ADJUST:
                newStock = currentStock + quantity;
                if (newStock < 0) {
                    throw new ValidationException("Estoque negativo não permitido");
                }
                break;
            default:
                throw new ValidationException("Tipo de movimiento inválido");
        }

        product.setCurrentStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}