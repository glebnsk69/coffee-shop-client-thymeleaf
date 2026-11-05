
package org.example.coffeeshopclient.dto;

import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id,
        String status,
        String statusName,
        LocalDateTime createdAt,
        Long durationSeconds
) {
}
