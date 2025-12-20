package com.porramat081.e_menu.service.orderItem;

import com.porramat081.e_menu.model.Order;
import com.porramat081.e_menu.model.OrderItem;
import com.porramat081.e_menu.model.Product;

public interface IOrderItemService {
    OrderItem createOrderItem(Product product, Order order, int quantity);
}
