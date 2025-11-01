package com.trendora.tienda.producto.service.interfaces;

import com.trendora.tienda.producto.dto.modelo.ModeloRequestDTO;
import com.trendora.tienda.producto.dto.modelo.ModeloResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IModeloService {

    List<ModeloResponseDTO> listAll();

    List<ModeloResponseDTO> findByMarcaId(Long id);

    Optional<ModeloResponseDTO> findById(Long id);

    ModeloResponseDTO create(ModeloRequestDTO modeloRequestDTO);

    Optional<ModeloResponseDTO> update(Long id, ModeloRequestDTO modeloRequestDTO);

    void delete(Long id);

    ModeloResponseDTO convertToResponseDTO(com.trendora.tienda.producto.model.Modelo modelo);
}
