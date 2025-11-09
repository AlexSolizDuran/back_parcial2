package com.trendora.tienda.producto.dto.producto;

import java.util.Set;

public record ProductoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        String imagen,
        Long modelo,
        Long categoria,
        Long material,
        Set<Long> etiquetas
) {

}
