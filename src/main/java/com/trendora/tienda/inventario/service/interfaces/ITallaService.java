package com.trendora.tienda.inventario.service.interfaces;

import com.trendora.tienda.inventario.dto.talla.TallaRequestDTO;
import com.trendora.tienda.inventario.dto.talla.TallaResponseDTO;
import com.trendora.tienda.inventario.model.Talla;

import java.util.List;
import java.util.Optional;

public interface ITallaService {
    List<TallaResponseDTO> listAll();
    Optional<TallaResponseDTO> findById(Long id);
    TallaResponseDTO create(TallaRequestDTO requestDTO);
    Optional<TallaResponseDTO> update(Long id, TallaRequestDTO requestDTO);
    void delete(Long id);
    TallaResponseDTO convertToResponseDTO(Talla talla);
}
