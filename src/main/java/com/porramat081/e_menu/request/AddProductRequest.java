package com.porramat081.e_menu.request;

import com.porramat081.e_menu.model.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String categoryName;

    public AddProductRequest(String productName, String productDes, BigDecimal productPrice, int productStock, String productCat) {
        this.name = productName;
        this.description = productDes;
        this.price = productPrice;
        this.stock = productStock;
        this.categoryName = productCat;
    }
}
