package com.trendora.tienda.config; // (Usa tu paquete de configuración)

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    // Aplica esta configuración a TODAS las rutas de tu API
                    .addMapping("/**") 
                    
                    // ¡CLAVE! Especifica la URL exacta de tu frontend
                    //.allowedOrigins("http://localhost:3000") 
                    .allowedOriginPatterns("*")
                    
                    // Métodos HTTP que permites
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    
                    // Cabeceras que permites
                    .allowedHeaders("*") 
                    
                    // ¡LA CLAVE! Esto es lo que permite que el navegador
                    // envíe la cookie HttpOnly
                    .allowCredentials(true); 
            }
        };
    }
}