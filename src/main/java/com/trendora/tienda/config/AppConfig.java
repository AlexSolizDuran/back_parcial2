package com.trendora.tienda.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature; // <-- Importa esto
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // <-- Importa esto
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // 1. REGISTRA EL MÓDULO DE JAVA 8 (PARA LOCALDATETIME)
        mapper.registerModule(new JavaTimeModule());
        
        // 2. (RECOMENDADO) ASEGURA QUE LAS FECHAS SE ESCRIBAN COMO TEXTO (ISO-8601)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return mapper;
    }
}
