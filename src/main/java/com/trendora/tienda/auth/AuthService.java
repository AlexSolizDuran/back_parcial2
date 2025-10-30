package com.trendora.tienda.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.trendora.tienda.auth.dto.AuthRequest;
import com.trendora.tienda.auth.dto.AuthResponse;
import com.trendora.tienda.usuario.dto.usuario.UsuarioRequestDTO;
import com.trendora.tienda.usuario.dto.usuario.UsuarioResponseDTO;
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
    public AuthResponse login(AuthRequest request) {
        // 1. Autenticamos
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), // <-- ¡CORREGIDO!
                        request.password()  // <-- ¡CORREGIDO!
                )
        );

        // 2. Cargamos UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username()); // <-- ¡TAMBIÉN AQUÍ!

        // 3. Generamos token
        final String jwt = jwtService.generateToken(userDetails);

        // 4. Devolvemos respuesta
        return new AuthResponse(jwt);
    }

    /**
     * Lógica de Registro: Llama al UsuarioService para crear el usuario.
     */
    public UsuarioResponseDTO register(UsuarioRequestDTO request) {
        // Delega la creación del usuario al servicio de usuarios (tu CRUD)
        return usuarioService.crearUsuario(request);
    }
}