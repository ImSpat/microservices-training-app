package com.example.ecommerce.order;

import org.springframework.stereotype.Service;

@Service
public class OrderMapper {

    public Order mapOrderRequestToOrder(OrderRequest request) {
        if (request == null) return null;
        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse mapOrderToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}
