package com.porramat081.e_menu.request;

import com.porramat081.e_menu.enums.PaymentGateway;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long amount;
    private String currency;
    private PaymentGateway gateway;
}
