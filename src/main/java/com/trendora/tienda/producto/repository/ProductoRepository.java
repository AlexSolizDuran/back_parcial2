package com.trendora.tienda.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendora.tienda.producto.model.Producto;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByModelo_Marca_Id(Long id);

    List<Producto> findByModeloId(Long id);

    List<Producto> findByCategoriaId(Long id);

    List<Producto> findByEtiquetasId(Long id);

    List<Producto> findByMaterialId(Long id);

}
