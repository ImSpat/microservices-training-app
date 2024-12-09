package com.example.ecommerce.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Long productId,
        String name,
        String description,
        BigDecimal price,
        double quantity
) {}
