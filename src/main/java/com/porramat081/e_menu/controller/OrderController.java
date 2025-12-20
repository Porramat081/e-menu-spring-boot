package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.enums.OrderStatus;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.*;
import com.porramat081.e_menu.repository.OrderItemRepository;
import com.porramat081.e_menu.repository.OrderRepository;
import com.porramat081.e_menu.request.CreateOrderRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.response.OrderSlip;
import com.porramat081.e_menu.response.PaymentResponse;
import com.porramat081.e_menu.service.payment.IPaymentService;
import com.porramat081.e_menu.service.product.IProductService;
import com.porramat081.e_menu.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api_prefix}/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final IProductService productService;
    private final IUserService userService;
    private final IPaymentService paymentService;

    @PostMapping("/order/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody CreateOrderRequest request){
        try {
//            User user = this.userService.getUserById(request.getUserId());

//            List<String> roles = user.getRoles()
//                    .stream()
//                    .map(Role::getName)
//                    .toList();
//
//            if(!roles.contains("ROLE_ADMIN")){
//
//                throw new Exception("this order not legit");
//            }

            LocalDateTime dateNow = LocalDateTime.now();

            Order inputOrder = new Order();
            inputOrder.setOrderDateTime(dateNow);
            inputOrder.setOrderStatus(OrderStatus.PENDING);
//            inputOrder.setUser(user);

            Order order = this.orderRepository.save(inputOrder);

            AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);

            Set<OrderItem> orderItems = request.getProductLists()
                    .stream()
                    .map(eachProduct -> {
                        try{
                            Product product = this.productService.getProductById(eachProduct.getProductId());
                            OrderItem orderItem = new OrderItem(
                                    order,
                                    product,
                                    eachProduct.getQuantity(),
                                    product.getPrice()
                            );

                            totalAmount.set(totalAmount.get().add(product.getPrice().multiply(BigDecimal.valueOf(eachProduct.getQuantity()))));

                            return  this.orderItemRepository.save(orderItem);
                        }catch(ResourceNotFoundException e){
                            return null;
                        }
            })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            BigDecimal result = totalAmount.get();

            order.setTotalAmount(result);
            order.getOrderItems().addAll(orderItems);
            this.orderRepository.save(order);
            return ResponseEntity.ok(new ApiResponse("Create Order Successfully",order));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId){
        try {
            Order order = this.orderRepository.findById(orderId)
                    .orElseThrow(()->new ResourceNotFoundException("Order not found"));
            PaymentResponse paymentResponse = this.paymentService.createPayment(order.getTotalAmount());
            System.out.println(order);
            System.out.println(paymentResponse.getPaymentRef());
            OrderSlip orderSlip = new OrderSlip(order,paymentResponse);
            return ResponseEntity.ok(new ApiResponse("Get order successfully",orderSlip));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }}
