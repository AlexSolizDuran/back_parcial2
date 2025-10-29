package com.trendora.tienda.usuario.dto.usuario;

import lombok.Data;

/**
 * DTO para OBTENER un Usuario (GET /{id}).
 * Muestra todos los detalles de un usuario, PERO OCULTA LA CONTRASEÑA.
 */
@Data
public class UsuarioResponseDTO {
    
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String telefono;
    private String rolNombre; // Muestra el nombre del rol, no el ID
    
    // --- ¡SIN CONTRASEÑA! ---
}