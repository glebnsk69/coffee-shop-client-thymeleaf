
package org.example.coffeeshopclient.service;

import org.example.coffeeshopclient.dto.CategoryResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CategoryApiClient {

    private final RestClient backendRestClient;

    public CategoryApiClient(RestClient backendRestClient) {
        this.backendRestClient = backendRestClient;
    }

    public List<CategoryResponse> getCategories(String token) {
        return backendRestClient.get()
                .uri("/api/categories")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
