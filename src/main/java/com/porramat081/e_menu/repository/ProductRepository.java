package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryNameIgnoreCase(String category);
    List<Product> findByNameIgnoreCase(String name);
    boolean existsByNameAndCategoryName(String name , String categoryName);
}
