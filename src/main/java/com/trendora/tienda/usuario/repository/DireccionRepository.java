package com.trendora.tienda.usuario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.usuario.model.Direccion;
import com.trendora.tienda.usuario.model.Usuario;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long>{
    Optional<Direccion> findByUsuario(Usuario usuario);
    Optional<Direccion> findByUsuarioId(Long id);
}
