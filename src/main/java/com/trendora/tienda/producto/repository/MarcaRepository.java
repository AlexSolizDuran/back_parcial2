package com.trendora.tienda.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.producto.model.Marca;

import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

}
