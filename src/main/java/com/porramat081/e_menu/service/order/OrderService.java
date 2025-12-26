package com.porramat081.e_menu.service.order;

import com.porramat081.e_menu.dto.EventDto;
import com.porramat081.e_menu.exception.ResourceNotFoundException;
import com.porramat081.e_menu.model.Order;
import com.porramat081.e_menu.repository.OrderRepository;
import com.porramat081.e_menu.request.UpdateOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Order updateExistingOrder(Order existingOrder , UpdateOrderRequest request){
        LocalDateTime dateNow = LocalDateTime.now();
        existingOrder.setUpdateDateTime(dateNow);
        existingOrder.setOrderStatus(request.getOrderStatus());
        return existingOrder;
    }

    @Override
    public Order updateOrder(UpdateOrderRequest request, Long orderId){

        EventDto event = new EventDto(orderId , "Order Updating" , "test upddate order " + orderId);

        messagingTemplate.convertAndSend("/test-ws/orders",event);

        return this.orderRepository.findById(orderId)
                .map(existingOrder -> updateExistingOrder(existingOrder,request))
                .map(this.orderRepository::save)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));
    }

}
