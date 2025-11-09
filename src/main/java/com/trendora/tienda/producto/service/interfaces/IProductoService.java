package com.trendora.tienda.producto.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.trendora.tienda.producto.dto.producto.ProductoRequestDTO; // Necesario para el convertToResponseDTO
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;
import com.trendora.tienda.producto.model.Producto;

public interface IProductoService {

    /**
     * Lista todos los productos.
     */
    List<ProductoResponseDTO> listAll();

    /**
     * Busca un producto por su ID.
     */
    Optional<ProductoResponseDTO> findById(Long id);

    /**
     * Crea un nuevo producto basado en el DTO de solicitud.
     */
    ProductoResponseDTO create(ProductoRequestDTO dto);

    /**
     * Actualiza un producto existente por su ID.
     */
    Optional<ProductoResponseDTO> update(Long id, ProductoRequestDTO dto);

    /**
     * Elimina un producto por su ID.
     */
    void delete(Long id);

    // --- Métodos de Búsqueda (Finders) ---

    List<ProductoResponseDTO> findByMarcaId(Long id);

    List<ProductoResponseDTO> findByModeloId(Long id);

    List<ProductoResponseDTO> findByMaterialId(Long id);

    List<ProductoResponseDTO> findByCategoriaId(Long id);

    List<ProductoResponseDTO> findByEtiquetaId(Long id);

    List<ProductoResponseDTO> findByCategoriaIdRecursive(Long id);
    /**
     * Método de utilidad para convertir una entidad Producto a un ProductoResponseDTO.
     * (Incluido porque la implementación lo tenía como @Override)
     */
    ProductoResponseDTO convertToResponseDTO(Producto producto);
}