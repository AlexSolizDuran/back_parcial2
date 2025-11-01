package com.trendora.tienda.inventario.repository;

import com.trendora.tienda.inventario.model.Talla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TallaRepository extends JpaRepository<Talla, Long> {
}
