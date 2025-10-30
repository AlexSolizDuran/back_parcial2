package com.trendora.tienda.auth.dto;

// Usamos records para DTOs simples (requiere Java 16+)
// Si usas una versión anterior, crea una clase normal con getters/setters.
public record AuthRequest(String username, String password) {
}