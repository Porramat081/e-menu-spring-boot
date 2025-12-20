package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.response.PaymentResponse;
import com.porramat081.e_menu.service.payment.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/test-payment")
public class TestPaymentController {

    private final IPaymentService paymentService;

    @GetMapping("/testing")
    public ResponseEntity<ApiResponse> testpayment(){
        try{
            PaymentResponse paymentResponse = this.paymentService.createPayment(BigDecimal.valueOf(2000));
            return ResponseEntity.ok(new ApiResponse("payment created" , paymentResponse));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse("Create Payment unsuccessful", null));
        }
    }

    @PostMapping("/webhook/omise")
    public ResponseEntity<Void> handleOmiseWebhook(@RequestBody String payload , @RequestHeader("X-Omise-Signature") String signature){
        System.out.println(signature);
        System.out.println(payload);
        return ResponseEntity.ok().build();
    }
}
