package com.trendora.tienda.producto.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.trendora.tienda.producto.dto.marca.MarcaRequestDTO;
import com.trendora.tienda.producto.dto.marca.MarcaResponseDTO;

public interface IMarcaService {

    List<MarcaResponseDTO> obtenerTodo();

    Optional<MarcaResponseDTO> obtenerPorId(Long id);

    MarcaResponseDTO guardar(MarcaRequestDTO marcaDTO);

    Optional<MarcaResponseDTO> actualizar(Long id, MarcaRequestDTO marcaDTO);

    void eliminar(Long id);
}
