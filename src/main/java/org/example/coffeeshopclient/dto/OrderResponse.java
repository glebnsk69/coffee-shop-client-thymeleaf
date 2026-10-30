
package org.example.coffeeshopclient.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        String createdAt,
        Long durationSeconds,
        List<OrderItemResponse> items
) {
}
