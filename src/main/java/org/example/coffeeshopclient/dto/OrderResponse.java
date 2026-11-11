
package org.example.coffeeshopclient.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        String statusName,
        String createdAt,
        Long durationSeconds,
        List<OrderItemResponse> items,
        List<String> allowedActions
) {
}
