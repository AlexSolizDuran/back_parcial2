package com.trendora.tienda.usuario.controller;

//
// ¡AQUÍ ESTÁ EL CAMBIO! La ruta al DTO es más específica.
//
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<UsuarioListDTO>> obtenerUsuarios( // <-- CAMBIO 1: De List a Page
            @RequestParam(name = "rol", required = false) String rolNombre,
            Pageable pageable // <-- CAMBIO 2: Añadir Pageable
    ) {
        
        Page<UsuarioListDTO> paginaDeUsuarios; // <-- CAMBIO 3: Usar Page<>

        if (rolNombre != null && !rolNombre.isEmpty()) {
            // CAMBIO 4: Llamar al servicio paginado
            paginaDeUsuarios = usuarioService.obtenerUsuariosPorRolPaginados(rolNombre, pageable);
        } else {
            // CAMBIO 5: Llamar al servicio paginado
            paginaDeUsuarios = usuarioService.obtenerUsuariosPaginados(pageable);
        }

        // Devolvemos la estructura Page<> completa, que SWR sabe cómo interpretar
        return ResponseEntity.ok(paginaDeUsuarios);
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