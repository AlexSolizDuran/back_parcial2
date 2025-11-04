package com.trendora.tienda.config.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trendora.tienda.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // <-- CAMBIO 1: Importar HttpHeaders

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Nuestro JpaUserDetailsService

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // --- INICIO DE CAMBIOS 2: Usar el nuevo método ---
        
        // Obtenemos el token usando nuestro método (que busca en cookies y header)
        final String jwt = getTokenFromRequest(request);

        if (jwt == null) {
            // Si no hay token, continuamos con el filtro y salimos
            filterChain.doFilter(request, response);
            return;
        }
        
        // --- FIN DE CAMBIOS 2 ---

        // El resto de la lógica para validar el token es la misma
        
        final String username;
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * Este es tu método para obtener el token.
     * Ahora SÍ se está usando.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        
        // 1. Buscar en las Cookies primero
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // El nombre debe coincidir con el del AuthService
                if ("jwt-token".equals(cookie.getName())) { 
                    return cookie.getValue();
                }
            }
        }
        
        // 2. (Opcional) Como fallback, buscar en el encabezado Authorization
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Usamos la constante
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null; // No se encontró el token
    }
}