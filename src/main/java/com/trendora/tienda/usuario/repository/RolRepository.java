package com.trendora.tienda.usuario.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.usuario.model.Rol;


@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // JpaRepository ya nos da el CRUD básico
    Optional<Rol> findByNombre(String nombre);

}