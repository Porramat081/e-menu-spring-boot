package com.porramat081.e_menu.request;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class CreateOrderRequest {
    //private Long userId;
    private List<ProductList> productLists;
}
