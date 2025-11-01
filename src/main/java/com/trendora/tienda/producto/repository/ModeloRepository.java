package com.trendora.tienda.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.producto.model.Modelo;

import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    List<Modelo> findByMarcaId(Long id);
}
