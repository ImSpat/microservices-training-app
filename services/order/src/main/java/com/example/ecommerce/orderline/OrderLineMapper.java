package com.example.ecommerce.orderline;

import com.example.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest request) {
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
}