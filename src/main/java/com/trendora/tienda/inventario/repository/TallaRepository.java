package com.trendora.tienda.inventario.repository;

import com.trendora.tienda.inventario.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {
    Optional<Talla> findByTalla(String talla);
}
