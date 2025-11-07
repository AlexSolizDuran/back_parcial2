package com.trendora.tienda.venta.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

//datos para mostrar al cliente
public record VentaResponseDTO(
    Long id,
    Long numeroVenta,
    Long clienteId,
    Long vendedorId,
    String metodoPago,
    String tipoVenta,
    Double montoTotal,
    String estadoPedido,
    LocalDateTime fechaVenta
) implements Serializable{}
