package com.beeleza.stockflow.repository;

import com.beeleza.stockflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
