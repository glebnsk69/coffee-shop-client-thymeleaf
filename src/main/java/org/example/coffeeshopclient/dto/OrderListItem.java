package org.example.coffeeshopclient.dto;

public record OrderListItem(
        Long id,
        String status,
        String statusName,
        long totalWithDiscount,
        long elapsedSeconds
) {
}