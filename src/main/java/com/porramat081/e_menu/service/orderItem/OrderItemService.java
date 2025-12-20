package com.porramat081.e_menu.service.orderItem;

import com.porramat081.e_menu.model.Order;
import com.porramat081.e_menu.model.OrderItem;
import com.porramat081.e_menu.model.Product;
import com.porramat081.e_menu.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService implements IOrderItemService{
    private final OrderItemRepository orderItemRepository;
    @Override
    public OrderItem createOrderItem(Product product, Order order, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(product.getPrice());
        return this.orderItemRepository.save(orderItem);
    }
}
