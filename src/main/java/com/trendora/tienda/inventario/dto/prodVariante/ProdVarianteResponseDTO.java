package com.trendora.tienda.inventario.dto.prodVariante;

import java.math.BigDecimal;

import com.trendora.tienda.inventario.dto.color.ColorResponseDTO;
import com.trendora.tienda.inventario.dto.talla.TallaResponseDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;

public record ProdVarianteResponseDTO(
        Long id,
        ProductoResponseDTO producto,
        ColorResponseDTO color,
        TallaResponseDTO talla,
        BigDecimal costo,
        BigDecimal ppp,
        BigDecimal ppv,
        BigDecimal precio,
        String sku,
        Integer stock
        ) {

        public boolean isLowStock() {
        // Puedes cambiar el '5' por la cantidad que consideres crítica
        return this.stock != null && this.stock > 0 && this.stock < 5;
    }
}
