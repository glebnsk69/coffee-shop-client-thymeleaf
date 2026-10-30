
package org.example.coffeeshopclient.dto;

public record ProductResponse(
        Long id,
        String name,
        int price,
        int discount,
        boolean active,
        Long categoryId,
        String imagePath,
        int priceWithDiscount,
        String stringForPrint
) {
}
