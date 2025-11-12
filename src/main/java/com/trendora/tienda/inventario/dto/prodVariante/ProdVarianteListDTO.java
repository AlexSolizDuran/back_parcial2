package com.trendora.tienda.inventario.dto.prodVariante;

import java.math.BigDecimal;

public record ProdVarianteListDTO(
        Long id,
        Long producto,
        Long color,
        Long talla,
        BigDecimal costo,
        BigDecimal precio,
        String sku,
        Integer stock
        ) {

}
