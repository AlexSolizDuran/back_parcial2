package com.trendora.tienda.inventario.service.interfaces;

import com.trendora.tienda.inventario.dto.color.ColorRequestDTO;
import com.trendora.tienda.inventario.dto.color.ColorResponseDTO;
import com.trendora.tienda.inventario.model.Color;

import java.util.List;
import java.util.Optional;

public interface IColorService {
    List<ColorResponseDTO> listAll();
    Optional<ColorResponseDTO> findById(Long id);
    ColorResponseDTO create(ColorRequestDTO requestDTO);
    Optional<ColorResponseDTO> update(Long id, ColorRequestDTO requestDTO);
    void delete(Long id);
    ColorResponseDTO convertToResponseDTO(Color color);
}
