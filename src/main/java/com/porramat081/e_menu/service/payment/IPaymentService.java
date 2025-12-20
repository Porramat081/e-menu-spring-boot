package com.porramat081.e_menu.service.payment;

import com.porramat081.e_menu.request.CreatePaymentRequest;
import com.porramat081.e_menu.response.PaymentResponse;

import java.math.BigDecimal;

public interface IPaymentService {
    PaymentResponse createPayment(BigDecimal amount) throws Exception;
}
