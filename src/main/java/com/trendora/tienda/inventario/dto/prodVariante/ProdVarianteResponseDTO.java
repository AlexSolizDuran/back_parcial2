package com.trendora.tienda.inventario.dto.prodVariante;

import com.trendora.tienda.inventario.dto.color.ColorResponseDTO;
import com.trendora.tienda.inventario.dto.talla.TallaResponseDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;

import java.math.BigDecimal;

public record ProdVarianteResponseDTO(
        Long id,
        ProductoResponseDTO producto,
        ColorResponseDTO color,
        TallaResponseDTO talla,
        BigDecimal costo,
        String imagen,
        BigDecimal ppp,
        BigDecimal precio,
        String sku,
        Integer stock
) {
}
