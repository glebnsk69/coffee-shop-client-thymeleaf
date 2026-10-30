package org.example.coffeeshopclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BackendConfig {

    @Bean
    public RestClient backendRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
}