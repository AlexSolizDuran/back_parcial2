package com.trendora.tienda.config; // (Puedes ponerlo en tu paquete de config)

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Lee la URL desde application.properties
    @Value("${ia.microservicio.url}")
    private String iaServiceUrl;

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl(iaServiceUrl) // Define la URL base para todas las llamadas
                .build();
    }
}