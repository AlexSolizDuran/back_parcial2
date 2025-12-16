package com.trendora.tienda.producto.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; // <--- AGREGAR
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trendora.tienda.producto.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByModelo_Marca_Id(Long id);

    List<Producto> findByModeloId(Long id);

    List<Producto> findByCategoriaId(Long id);

    List<Producto> findByEtiquetasId(Long id);

    List<Producto> findByMaterialId(Long id);

    Optional<Producto> findByDescripcion(String descripcion);

    List<Producto> findByCategoriaIdIn(Collection<Long> ids);

    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.modelo.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.modelo.marca.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Producto> search(@Param("query") String query);
}
