package com.porramat081.e_menu.response;

import com.porramat081.e_menu.dto.ProductDto;
import com.porramat081.e_menu.enums.OrderStatus;
import com.porramat081.e_menu.model.Order;
import com.porramat081.e_menu.model.OrderItem;
import com.porramat081.e_menu.service.product.IProductService;
import com.porramat081.e_menu.service.product.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class OrderSlip {
    private Long id;
    private Set<OrderItem> orderItems;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private String orderRef;
    private int queue;
    private PaymentResponse paymentResponse;

    public OrderSlip(Order order , PaymentResponse paymentResponse){
        this.id = order.getId();
        this.orderItems = order.getOrderItems();
        this.orderDateTime = order.getOrderDateTime();
        this.orderStatus = order.getOrderStatus();
        this.orderRef = order.getOrderRef();
        this.queue = order.getQueue();
        this.paymentResponse = paymentResponse;
    }
}
