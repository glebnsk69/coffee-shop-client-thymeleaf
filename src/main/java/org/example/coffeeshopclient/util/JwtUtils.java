package org.example.coffeeshopclient.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class JwtUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getRole(String token) {
        try {
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT");
            }

            String payload = new String(
                    Base64.getUrlDecoder().decode(parts[1])
            );

            JsonNode json = objectMapper.readTree(payload);

//            return json.get("roles").get(0).asText().replace("ROLE_", "");

            return switch (json.get("roles").get(0).asText()) {
                case "ROLE_ADMIN" -> "Администратор";
                case "ROLE_CASHIER" -> "Кассир";
                case "ROLE_GUEST" -> "Гость";
                default -> "Неизвестная роль";
            };

        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot read JWT role", e);
        }
    }
}