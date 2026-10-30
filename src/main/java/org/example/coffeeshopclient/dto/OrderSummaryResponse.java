
package org.example.coffeeshopclient.dto;

import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id,
        String status,
        LocalDateTime createdAt,
        Long durationSeconds,
        boolean deleted
) {
}
