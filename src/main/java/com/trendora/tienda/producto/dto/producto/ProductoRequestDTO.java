package com.trendora.tienda.producto.dto.producto;

import java.util.Set;

public record ProductoRequestDTO(
        String descripcion,
        Long modelo,
        Long categoria,
        Long material,
        Set<Long> etiquetas
) {

}
