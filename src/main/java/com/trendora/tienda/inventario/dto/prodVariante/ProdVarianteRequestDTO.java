package com.trendora.tienda.inventario.dto.prodVariante;

import java.math.BigDecimal;

public record ProdVarianteRequestDTO(
        Long productoId,
        Long colorId,
        Long tallaId,
        BigDecimal costo,
        String imagen,
        BigDecimal ppp,
        BigDecimal precio,
        String sku,
        Integer stock
) {
}
