package com.trendora.tienda.producto.repository;

import com.trendora.tienda.producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByModelo_Marca_Id(Long id);

    List<Producto> findByModeloId(Long id);

    List<Producto> findByCategoriaId(Long id);

    List<Producto> findByEtiquetasId(Long id);

    List<Producto> findByMaterialId(Long id);

    Optional<Producto> findByDescripcion(String descripcion);

    List<Producto> findByCategoriaIdIn(Collection<Long> ids);
}
