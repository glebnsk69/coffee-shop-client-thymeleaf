package org.example.coffeeshopclient.service;

import org.example.coffeeshopclient.dto.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.example.coffeeshopclient.dto.ProductResponse;
import java.util.List;

@Service
public class ProductApiClient {

    private final RestClient backendRestClient;

    public ProductApiClient(RestClient backendRestClient) {
        this.backendRestClient = backendRestClient;
    }
    public List<ProductResponse> getProducts(String token) {
        return backendRestClient.get()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
    public String getProductsJson(String token) {
        return backendRestClient.get()
                .uri("/api/products")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(String.class);
    }

    public ResponseEntity<byte[]> getProductImage(Long id, String token) {
        return backendRestClient.get()
                .uri("/api/products/{id}/image", id)
                .header("Authorization", "Bearer " + token)
                .exchange((request, response) -> {
                    byte[] image = response.getBody().readAllBytes();
                    MediaType contentType = response.getHeaders().getContentType();

                    return ResponseEntity
                            .status(response.getStatusCode())
                            .contentType(contentType != null
                                    ? contentType
                                    : MediaType.APPLICATION_OCTET_STREAM)
                            .body(image);
                });
    }

}