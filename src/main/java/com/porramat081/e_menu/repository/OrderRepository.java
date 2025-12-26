package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order , Long> {
    @Query("""
    SELECT COALESCE(MAX(o.queue), 0)
    FROM Order o
    WHERE o.updateDateTime >= :start
      AND o.updateDateTime < :end
""")
    int findMaxQueueToday(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    Optional<Order> findTopByUpdateDateTimeGreaterThanEqualAndUpdateDateTimeLessThanOrderByUpdateDateTimeDesc(
            LocalDateTime startOfDay,
            LocalDateTime startOfNextDay
    );
    List<Order> findByUpdateDateTimeGreaterThanEqualAndUpdateDateTimeLessThan(LocalDateTime from , LocalDateTime to , Sort sort);
}
