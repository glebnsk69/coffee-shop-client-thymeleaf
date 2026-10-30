package org.example.coffeeshopclient.service;

import org.example.coffeeshopclient.dto.LoginRequest;
import org.example.coffeeshopclient.dto.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthApiClient {

    private final RestClient backendRestClient;

    public AuthApiClient(RestClient backendRestClient) {
        this.backendRestClient = backendRestClient;
    }

    public LoginResponse login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);

        String json = backendRestClient.post()
                .uri("/api/auth/login")
                .body(request)
                .retrieve()
                .body(String.class);

        System.out.println("Login JSON: " + json);

        return backendRestClient.post()
                .uri("/api/auth/login")
                .body(request)
                .retrieve()
                .body(LoginResponse.class);
    }
}
