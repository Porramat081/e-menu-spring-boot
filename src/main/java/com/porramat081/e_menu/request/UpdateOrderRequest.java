package com.porramat081.e_menu.request;

import com.porramat081.e_menu.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderRequest {
    private OrderStatus orderStatus;
}
