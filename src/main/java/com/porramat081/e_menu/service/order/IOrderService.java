package com.porramat081.e_menu.service.order;

import com.porramat081.e_menu.model.Order;
import com.porramat081.e_menu.request.UpdateOrderRequest;

public interface IOrderService {
    Order updateOrder(UpdateOrderRequest request, Long orderId);
}
