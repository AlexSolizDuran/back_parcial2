package com.trendora.tienda.venta.dto.detalleventa;

import java.io.Serializable;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;

public record DetalleVentaResponseDTO(
    Long id,
    Integer cantidad,
    Double precio_unit,
    Double descuento,
    Double subtotal,
    Long ventaId,
    ProdVarianteResponseDTO prodVariante

) implements Serializable{}
