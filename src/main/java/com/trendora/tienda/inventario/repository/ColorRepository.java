package com.trendora.tienda.inventario.repository;

import com.trendora.tienda.inventario.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    Optional<Color> findByNombre(String nombre);
}
