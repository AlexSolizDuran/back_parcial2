package com.trendora.tienda.venta.dto.detalleventa;

import java.io.Serializable;

public record DetalleVentaRequestDTO(
    Integer cantidad,
    Double precio_unit,
    Double descuento,
    Double subtotal,
    Long ventaId,
    Long prodVarianteId
) implements Serializable{}
