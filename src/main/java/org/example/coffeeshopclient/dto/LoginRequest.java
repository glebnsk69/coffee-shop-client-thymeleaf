package org.example.coffeeshopclient.dto;

public record LoginRequest(
        String username,
        String password
) {
}
