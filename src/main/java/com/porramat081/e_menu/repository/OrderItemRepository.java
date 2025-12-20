package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
