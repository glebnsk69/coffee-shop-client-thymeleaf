package org.example.coffeeshopclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("accessToken") String token,
        String tokenType,
        long expiresIn
) {
}
