package com.example.ecommerce.orderline;

import com.example.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine mapOrderLineRequestToOrderLine(OrderLineRequest request) {
        return OrderLine.builder()
                .id(request.orderId())
                .productId(request.productId())
                .order(Order.builder()
                        .id(request.orderId())
                        .build()
                )
                .quantity(request.quantity())
                .build();
    }

    public OrderLineResponse mapOrderLineToOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
