package com.trendora.tienda.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.producto.model.Etiqueta;

import org.springframework.stereotype.Repository;

@Repository
public interface EtiquetaRepository extends JpaRepository<Etiqueta, Long> {

    List<Etiqueta> findByProductosId(Long id);
}
