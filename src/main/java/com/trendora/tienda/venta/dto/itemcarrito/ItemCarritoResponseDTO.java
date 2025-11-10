package com.trendora.tienda.venta.dto.itemcarrito;

import java.io.Serializable;

import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;

//lo que se da
public record ItemCarritoResponseDTO(
        Long id,
        Long carritoId,
        ProdVarianteResponseDTO prodVariante,
        Integer cantidad
        ) implements Serializable {

}
