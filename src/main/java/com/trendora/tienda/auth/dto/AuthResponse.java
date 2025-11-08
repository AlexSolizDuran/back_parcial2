// Este es el archivo: AuthResponse.java
package com.trendora.tienda.auth.dto;

import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;

// Esta es la "fuente única de la verdad"
public record AuthResponse(
    String token,
    UsuarioResponseDTO usuario
    
) {}