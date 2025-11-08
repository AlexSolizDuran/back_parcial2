package com.trendora.tienda.inventario.dto.prodVariante;

import java.math.BigDecimal;

public record ProdVarianteList(
        Long id,
        String producto,
        String color,
        String talla,
        BigDecimal costo,
        String imagen,
        BigDecimal ppp,
        BigDecimal precio,
        String sku,
        Integer stock
        ) {

}
