package com.trendora.tienda.auth;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.trendora.tienda.auth.dto.AuthRequest;
import com.trendora.tienda.auth.dto.AuthResponse;
import com.trendora.tienda.usuario.dto.usuario.UsuarioRequestDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;
import com.trendora.tienda.usuario.model.Usuario;
import com.trendora.tienda.usuario.service.UsuarioService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioService usuarioService; // El servicio del CRUD

    public AuthService(AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService,
            UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    /**
     * Lógica de Login: Autentica y devuelve un token.
     */
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // 1. Autenticamos (Esto queda igual)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Cargamos UserDetails (Esto queda igual)
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // 3. Generamos token (Esto queda igual)
        final String jwt = jwtService.generateToken(userDetails);

        // 4. --- ¡AQUÍ ESTÁ EL CAMBIO! ---
        // Creamos la cookie HttpOnly
        ResponseCookie jwtCookie = ResponseCookie.from("jwt-token", jwt)
                .httpOnly(true) // 🔒 ¡Crítico! Previene XSS
                .secure(false) // 🔒 ¡Recuerda! Poner en 'true' para producción (HTTPS)
                .path("/") // Disponible en toda la app
                .maxAge(24 * 60 * 60) // Tiempo de vida (ej. 86400s = 24h)
                .sameSite("Lax") // 🛡️ ¡Crítico! Protección CSRF
                .build();

        // 5. Preparamos el cuerpo de la respuesta con los datos del usuario
        // (Asumimos que tu UserDetails es tu entidad 'Usuario')
        Usuario usuario = (Usuario) userDetails;
        var roles = userDetails.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()); // o .toList() en Java 16+

        AuthResponse responseBody = new AuthResponse(
                jwt,
                usuario.getId(), // Asumiendo que tienes .getId()
                usuario.getUsername(),
                roles
        );

        // 6. Devolvemos el ResponseEntity
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString()) // <-- Aquí se adjunta la cookie
                .body(responseBody); // <-- Aquí van los datos del usuario
    }

    /**
     * Lógica de Registro: Llama al UsuarioService para crear el usuario.
     */
    public UsuarioResponseDTO register(UsuarioRequestDTO request) {
        // Delega la creación del usuario al servicio de usuarios (tu CRUD)
        return usuarioService.crearUsuario(request);
    }
}
