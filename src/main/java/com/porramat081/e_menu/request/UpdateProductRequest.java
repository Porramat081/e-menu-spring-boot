package com.porramat081.e_menu.request;

import com.porramat081.e_menu.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Category category;

    public UpdateProductRequest(Long id, String productName, String productDes, BigDecimal productPrice, int productStock, Category productCat) {
        this.id =  id;
        this.name = productName;
        this.description = productDes;
        this.price = productPrice;
        this.stock = productStock;
        this.category =  productCat;
    }
}
