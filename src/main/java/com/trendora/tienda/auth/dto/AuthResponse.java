// Este es el archivo: AuthResponse.java
package com.trendora.tienda.auth.dto;

import java.util.Collection;

// Esta es la "fuente única de la verdad"
public record AuthResponse(
    String token,
    Long userId, 
    String username, 
    Collection<String> roles
) {}