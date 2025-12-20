package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query(value= """
            SELECT * FROM image
            WHERE product_id = :productId
            """,nativeQuery = true)
    List<Image> findByProductId(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM image WHERE product_id = :productId
            """ , nativeQuery = true)
    void deleteByProductId(@Param("productId") Long productId);
}
