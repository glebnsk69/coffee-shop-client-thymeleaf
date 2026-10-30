
package org.example.coffeeshopclient.controller;

import jakarta.servlet.http.HttpSession;
import org.example.coffeeshopclient.service.ProductApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductImageController {

    private final ProductApiClient productApiClient;

    public ProductImageController(ProductApiClient productApiClient) {
        this.productApiClient = productApiClient;
    }

    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getProductImage(
            @PathVariable Long id,
            HttpSession session) {

        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        return productApiClient.getProductImage(id, token);
    }
}
