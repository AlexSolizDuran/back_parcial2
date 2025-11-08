package com.trendora.tienda.auth;

import java.util.HashMap;
import java.util.Map;

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
        Usuario usuarioCompleto = (Usuario) userDetails;
        String rolNombre = usuarioCompleto.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .findFirst() // Tomamos solo el primer rol
                .orElse(null); // O un valor por defecto si lo prefieres
        // 3. Generamos token (Esto queda igual)
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", rolNombre);
        final String jwt = jwtService.generateToken(extraClaims,userDetails);

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

        // 5. Creamos el DTO con TODOS los campos de tu entidad
        UsuarioResponseDTO userDto = new UsuarioResponseDTO();
        userDto.setId(usuarioCompleto.getId());
        userDto.setNombre(usuarioCompleto.getNombre());
        userDto.setApellido(usuarioCompleto.getApellido());
        userDto.setEmail(usuarioCompleto.getEmail());
        userDto.setUsername(usuarioCompleto.getUsername());
        userDto.setTelefono(usuarioCompleto.getTelefono());
        userDto.setRolNombre(rolNombre);

        AuthResponse responseBody = new AuthResponse(
                jwt,
                userDto
        );
        System.out.println(responseBody);

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
