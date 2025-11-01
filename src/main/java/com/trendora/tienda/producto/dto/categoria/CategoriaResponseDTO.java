package com.trendora.tienda.producto.dto.categoria;

public record CategoriaResponseDTO(
        Long id,
        String nombre,
        Long padreId
) {
}