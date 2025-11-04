package com.trendora.tienda.producto.service.interfaces;

import com.trendora.tienda.producto.dto.producto.ProductoRequestDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;
import com.trendora.tienda.producto.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

    List<ProductoResponseDTO> listAll();

    List<ProductoResponseDTO> findByMarcaId(Long id);

    List<ProductoResponseDTO> findByModeloId(Long id);

    List<ProductoResponseDTO> findByMaterialId(Long id);

    List<ProductoResponseDTO> findByCategoriaId(Long id);

    List<ProductoResponseDTO> findByEtiquetaId(Long id);

    Optional<ProductoResponseDTO> findById(Long id);

    ProductoResponseDTO create(ProductoRequestDTO productoRequestDTO);

    Optional<ProductoResponseDTO> update(Long id, ProductoRequestDTO productoRequestDTO);

    void delete(Long id);

    ProductoResponseDTO convertToResponseDTO(Producto producto);
}
