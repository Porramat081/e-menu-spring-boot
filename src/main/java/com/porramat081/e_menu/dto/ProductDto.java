package com.porramat081.e_menu.dto;

import com.porramat081.e_menu.model.Category;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

    private Category category;

    private List<ImageDto> images;
}
