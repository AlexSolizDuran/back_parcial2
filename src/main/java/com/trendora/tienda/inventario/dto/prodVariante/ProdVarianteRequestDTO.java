package com.trendora.tienda.inventario.dto.prodVariante;

import java.math.BigDecimal;

public record ProdVarianteRequestDTO(
        Long producto,
        Long color,
        Long talla,
        BigDecimal costo,
        BigDecimal precio,
        String sku,
        Integer stock
) {
}
