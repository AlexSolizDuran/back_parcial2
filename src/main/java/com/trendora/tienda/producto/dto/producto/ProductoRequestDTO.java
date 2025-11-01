package com.trendora.tienda.producto.dto.producto;

import java.util.Set;

public record ProductoRequestDTO(
        String descripcion,
        Long modeloId,
        Long categoriaId,
        Long materialId,
        Set<Long> etiquetaIds
) {

}
