
package org.example.coffeeshopclient.service;

import org.example.coffeeshopclient.dto.*;
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

    public void changeOrderStatus(Long id, String token, ChangeOrderStatusRequest request) {
        backendRestClient.patch()
                .uri("/api/orders/{id}/status", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
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
    List<OrderStatusOption> availableStatuses;


//    System.out.println("==>Order: " + order);
    return order;
}


/*
    public List<OrderStatusOption> getAvailableStatuses(Long orderId, String token) {
        return backendRestClient.get()
                .uri("/api/orders/{id}/available-statuses", orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
*/
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
    public List<OrderSummaryResponse> getOrders() {
        return backendRestClient.get()
                .uri("/api/orders")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public OrderResponse getOrder(Long id) {
        return backendRestClient.get()
                .uri("/api/orders/{id}", id)
                .retrieve()
                .body(OrderResponse.class);
    }




}
