package com.porramat081.e_menu.repository;

import com.porramat081.e_menu.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order , Long> {

}
