package com.trendora.tienda.inventario.repository;

import com.trendora.tienda.inventario.model.ProdVariante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdVarianteRepository extends JpaRepository<ProdVariante, Long> {
    List<ProdVariante> findByProductoId(Long productoId);
}