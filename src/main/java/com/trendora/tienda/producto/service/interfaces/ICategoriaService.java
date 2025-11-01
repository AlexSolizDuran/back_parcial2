package com.trendora.tienda.producto.service.interfaces;

import com.trendora.tienda.producto.dto.categoria.CategoriaRequestDTO;
import com.trendora.tienda.producto.dto.categoria.CategoriaResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    List<CategoriaResponseDTO> listAll();

    Optional<CategoriaResponseDTO> findById(Long id);

    CategoriaResponseDTO create(CategoriaRequestDTO categoriaRequestDTO);

    Optional<CategoriaResponseDTO> update(Long id, CategoriaRequestDTO categoriaRequestDTO);

    void delete(Long id);

    CategoriaResponseDTO convertToResponseDTO(com.trendora.tienda.producto.model.Categoria categoria);
}
