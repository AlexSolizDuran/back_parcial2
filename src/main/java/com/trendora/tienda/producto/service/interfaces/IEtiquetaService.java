package com.trendora.tienda.producto.service.interfaces;

import com.trendora.tienda.producto.dto.etiqueta.EtiquetaRequestDTO;
import com.trendora.tienda.producto.dto.etiqueta.EtiquetaResponseDTO;

import java.util.List;
import java.util.Optional;

public interface IEtiquetaService {

    List<EtiquetaResponseDTO> listAll();

    List<EtiquetaResponseDTO> findByProductoId(Long id);

    Optional<EtiquetaResponseDTO> findById(Long id);

    EtiquetaResponseDTO create(EtiquetaRequestDTO etiquetaRequestDTO);

    Optional<EtiquetaResponseDTO> update(Long id, EtiquetaRequestDTO etiquetaRequestDTO);

    void delete(Long id);

    EtiquetaResponseDTO convertToResponseDTO(com.trendora.tienda.producto.model.Etiqueta etiqueta);
}
