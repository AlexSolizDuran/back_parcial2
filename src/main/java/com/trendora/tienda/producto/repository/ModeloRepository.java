package com.trendora.tienda.producto.repository;

import com.trendora.tienda.producto.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    List<Modelo> findByMarcaId(Long id);
    Optional<Modelo> findByNombre(String nombre);
}
