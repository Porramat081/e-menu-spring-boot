package com.porramat081.e_menu.model;

import com.porramat081.e_menu.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDateTime;
    private LocalDateTime updateDateTime;
    private BigDecimal totalAmount;

    private String orderRef;

    private int queue;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}
