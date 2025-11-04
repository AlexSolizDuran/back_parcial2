package com.trendora.tienda.producto.service.interfaces;

import com.trendora.tienda.producto.dto.material.MaterialReponseDTO;
import com.trendora.tienda.producto.dto.material.MaterialRequestDTO;

import java.util.List;
import java.util.Optional;

public interface IMaterialService {

    List<MaterialReponseDTO> listAll();

    Optional<MaterialReponseDTO> findById(Long id);

    MaterialReponseDTO create(MaterialRequestDTO materialRequestDTO);

    Optional<MaterialReponseDTO> update(Long id, MaterialRequestDTO materialRequestDTO);

    void delete(Long id);

    MaterialReponseDTO convertToResponseDTO(com.trendora.tienda.producto.model.Material material);
}
