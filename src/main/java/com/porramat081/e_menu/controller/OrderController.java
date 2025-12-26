package com.porramat081.e_menu.controller;

import com.porramat081.e_menu.enums.OrderStatus;
import com.porramat081.e_menu.enums.PaymentGateway;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.*;
import com.porramat081.e_menu.repository.OrderItemRepository;
import com.porramat081.e_menu.repository.OrderRepository;
import com.porramat081.e_menu.request.CreateOrderRequest;
import com.porramat081.e_menu.request.UpdateOrderRequest;
import com.porramat081.e_menu.response.ApiResponse;
import com.porramat081.e_menu.response.OrderSlip;
import com.porramat081.e_menu.response.PaymentResponse;
import com.porramat081.e_menu.service.order.IOrderService;
import com.porramat081.e_menu.service.payment.IPaymentService;
import com.porramat081.e_menu.service.product.IProductService;
import com.porramat081.e_menu.service.user.IUserService;
import com.porramat081.e_menu.utils.AesTextEncryption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final IOrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestBody CreateOrderRequest request){
        try {
            LocalDateTime dateNow = LocalDateTime.now();

            Order inputOrder = new Order();
            inputOrder.setOrderDateTime(dateNow);
            inputOrder.setUpdateDateTime(dateNow);
            inputOrder.setOrderStatus(OrderStatus.PENDING);

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

            SecretKey key = AesTextEncryption.generateKey();
            String orderRef = AesTextEncryption.encrypt(String.valueOf(order.getId()) + " //// " +key , key );
            order.setOrderRef(orderRef);

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

            OrderSlip orderSlip = new OrderSlip(order,paymentResponse);
            return ResponseEntity.ok(new ApiResponse("Get order successfully",orderSlip));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-client")
    public ResponseEntity<ApiResponse> getClientOrder(@RequestParam String orderRef){
        try{
            String reg = " //// ";
            String[] splitText = orderRef.split(reg);
            String decryptText = AesTextEncryption.decrypt(splitText[0],splitText[1]);
            Order clientOrder = this.orderRepository.findById(Long.parseLong(decryptText)).orElseThrow(()->new ResourceNotFoundException("Order not found"));
            return ResponseEntity.ok(new ApiResponse("Get Order Successfully" , clientOrder));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Order not found" ,null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllOrder(@RequestParam LocalDate fromDate , @RequestParam LocalDate toDate){
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.plusDays(1).atStartOfDay();
        List<Order> orders = this.orderRepository.findByUpdateDateTimeGreaterThanEqualAndUpdateDateTimeLessThan(from,to,Sort.by(Sort.Direction.DESC,"updateDateTime"));
        return ResponseEntity.ok(new ApiResponse("Get Order Successfully",orders));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long orderId){
        try{
            this.orderRepository.findById(orderId).ifPresentOrElse(this.orderRepository::delete,
                    ()->{
                        throw new ResourceNotFoundException("Order not found");
                    }
            );
            return  ResponseEntity.ok(new ApiResponse("Order is deleted successfully",null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{orderId}/update")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long orderId , @RequestBody UpdateOrderRequest request){
        try{
            Order updatedOrder = this.orderService.updateOrder(request,orderId);
            return ResponseEntity.ok(new ApiResponse("Update Order Successfully",updatedOrder));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{orderId}/send-pay")
    public ResponseEntity<ApiResponse> sendPay(@PathVariable Long orderId , @RequestParam PaymentGateway paymentGateway){
        try{
            Order selectedOrder = this.orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
            LocalDate today = LocalDate.now();

            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();
            int nextSeq = this.orderRepository.findMaxQueueToday(startOfDay,startOfNextDay) + 1;

            selectedOrder.setQueue(nextSeq);

            selectedOrder.setOrderStatus(OrderStatus.PAID);
            this.orderRepository.save(selectedOrder);
            return ResponseEntity.ok(new ApiResponse("Send Request For Payment",selectedOrder));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}


