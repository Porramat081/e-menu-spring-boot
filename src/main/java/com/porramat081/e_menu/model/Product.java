package com.porramat081.e_menu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private List<Image> images;

    public Product(String name, String description, BigDecimal price, int stock, Category category, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.createdAt = date;
        this.updatedAt = date;
    }
}
