
package org.example.coffeeshopclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record OrderItemResponse(
        Long productId,
        @JsonProperty("productName") String name,
        int cost,
        int discount,
        int quantity
) {
    public long discountedPrice() {
        return BigDecimal.valueOf(cost)
                .multiply(BigDecimal.valueOf(100 - discount))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                .longValueExact();
    }
}
