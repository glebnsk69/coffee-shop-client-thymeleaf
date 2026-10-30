package org.example.coffeeshopclient.dto;

public record CreateOrderItemRequest(
        Long productId,
        int quantity
) {}