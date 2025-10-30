package com.trendora.tienda.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.auth.dto.AuthRequest;
import com.trendora.tienda.auth.dto.AuthResponse;
import com.trendora.tienda.usuario.dto.usuario.UsuarioRequestDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;

@RestController
@RequestMapping("/auth") 
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para autenticar un usuario y obtener un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }
}