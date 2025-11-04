package com.trendora.tienda.producto.dto.producto;

import java.util.Set;

import com.trendora.tienda.producto.dto.categoria.CategoriaResponseDTO;
import com.trendora.tienda.producto.dto.etiqueta.EtiquetaResponseDTO;
import com.trendora.tienda.producto.dto.material.MaterialReponseDTO;
import com.trendora.tienda.producto.dto.modelo.ModeloResponseDTO;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        ModeloResponseDTO modelo,
        CategoriaResponseDTO categoria,
        MaterialReponseDTO material,
        Set<EtiquetaResponseDTO> etiquetas
) {

}
