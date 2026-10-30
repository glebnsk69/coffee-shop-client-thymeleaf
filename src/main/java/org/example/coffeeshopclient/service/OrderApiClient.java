
package org.example.coffeeshopclient.service;

import org.example.coffeeshopclient.dto.CreateOrderRequest;
import org.example.coffeeshopclient.dto.OrderResponse;
import org.example.coffeeshopclient.dto.OrderSummaryResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;


import java.util.List;

@Service
public class OrderApiClient {

    private final RestClient backendRestClient;

    public OrderApiClient(RestClient backendRestClient) {
        this.backendRestClient = backendRestClient;
    }
/*
    public OrderResponse getOrder(Long id, String token) {
        return backendRestClient.get()
                .uri("/api/orders/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(OrderResponse.class);
    }
    */
public OrderResponse getOrder(Long id, String token) {
    OrderResponse order = backendRestClient.get()
            .uri("/api/orders/{id}", id)
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .body(OrderResponse.class);

    System.out.println("==>Order: " + order);
    return order;
}
    public List<OrderSummaryResponse> getOrders(String token) {
        return backendRestClient.get()
                .uri("/api/orders")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void createOrder(String token, CreateOrderRequest request) {
        backendRestClient.post()
                .uri("/api/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

}
