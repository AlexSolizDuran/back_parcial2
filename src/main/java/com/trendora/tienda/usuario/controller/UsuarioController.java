package com.trendora.tienda.usuario.controller;

//
// ¡AQUÍ ESTÁ EL CAMBIO! La ruta al DTO es más específica.
//
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.usuario.dto.usuario.UsuarioListDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioRequestDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;
import com.trendora.tienda.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @Valid @RequestBody UsuarioRequestDTO requestDTO
    ) {
        UsuarioResponseDTO responseDTO = usuarioService.crearUsuario(requestDTO);
        return ResponseEntity.created(URI.create("/usuario/usuario/" + responseDTO.getId()))
                             .body(responseDTO);
    }

  @GetMapping
    public ResponseEntity<List<UsuarioListDTO>> obtenerUsuarios(
            @RequestParam(name = "rol", required = false) String rolNombre
    ) {
        List<UsuarioListDTO> usuarios;

        if (rolNombre != null && !rolNombre.isEmpty()) {
            usuarios = usuarioService.obtenerUsuariosPorRol(rolNombre);
        } else {
            usuarios = usuarioService.obtenerTodosLosUsuarios();
        }

        return ResponseEntity.ok(usuarios);
    }

    // --- READ (Uno por ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDTO responseDTO = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO requestDTO
    ) {
        UsuarioResponseDTO responseDTO = usuarioService.actualizarUsuario(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}