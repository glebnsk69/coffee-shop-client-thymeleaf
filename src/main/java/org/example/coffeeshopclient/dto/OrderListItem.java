package org.example.coffeeshopclient.dto;

public record OrderListItem(
        Long id,
        String status,
        long totalWithDiscount,
        long elapsedSeconds
) {
}