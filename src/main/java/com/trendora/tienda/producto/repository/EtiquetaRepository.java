package com.trendora.tienda.producto.repository;

import com.trendora.tienda.producto.model.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    List<Etiqueta> findByProductosId(Long id);
    Optional<Etiqueta> findByNombre(String nombre);
}
