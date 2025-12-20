package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    List<Promotion> findByStartDateLessThanEqualAndStopDateGreaterThanEqual(
            LocalDateTime startDate,
            LocalDateTime stopDate
    );
}
