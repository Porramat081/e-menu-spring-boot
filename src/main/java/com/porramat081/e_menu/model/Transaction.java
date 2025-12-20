package com.porramat081.e_menu.model;

import com.porramat081.e_menu.enums.PaymentGateway;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime transactDateTime;

    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    private String paymentRef;
}
