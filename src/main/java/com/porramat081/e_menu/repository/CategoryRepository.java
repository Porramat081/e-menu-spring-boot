package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);
    boolean existsByName(String name);
    @Query("""
       SELECT c FROM Category c 
       WHERE LOWER(c.name) <> LOWER('uncategory')
       AND size(c.products) > 0
       ORDER BY LOWER(c.name) ASC
       """)
    List<Category> findAllNonEmptyCategories();
    //List<Category> findByNameNot(String name , Sort sort);
}
