package com.beeleza.stockflow.repository;

import com.beeleza.stockflow.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
