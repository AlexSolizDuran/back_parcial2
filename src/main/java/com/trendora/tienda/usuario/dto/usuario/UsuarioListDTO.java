package com.trendora.tienda.usuario.dto.usuario;

import lombok.Data;

/**
 * DTO para LISTAR usuarios (GET /).
 * Es una versión ligera del DTO de respuesta, optimizada para listas.
 * Solo incluye la información esencial.
 */
@Data
public class UsuarioListDTO {
    
    private Long id;
    private String username;
    private String nombre;
    private String email;
    private String rolNombre;
    
    // --- ¡SIN CONTRASEÑA, APELLIDO, NI TELÉFONO! ---
}