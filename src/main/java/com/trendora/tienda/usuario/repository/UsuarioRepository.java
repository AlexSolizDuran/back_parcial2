package com.trendora.tienda.usuario.repository;

import com.trendora.tienda.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método útil para buscar por email o username (para el login)
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
}