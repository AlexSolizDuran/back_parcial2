package com.trendora.tienda.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.producto.model.Material;

import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

}
