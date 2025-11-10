package com.trendora.tienda.usuario.dto.usuario;

import lombok.Data;

/**
 * DTO para LISTAR usuarios (GET /). Es una versión ligera del DTO de respuesta,
 * optimizada para listas. Solo incluye la información esencial.
 */
@Data
public class UsuarioListDTO {

    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String rolNombre;



    public UsuarioListDTO(Long id, String username, String nombre, String apellido, String email, String rolNombre) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rolNombre = rolNombre;
    }
    // --- ¡SIN CONTRASEÑA, APELLIDO, NI TELÉFONO! ---
}
