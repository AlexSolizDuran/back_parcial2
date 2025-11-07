package com.trendora.tienda.venta.dto.carrito;

import java.io.Serializable;
import java.time.LocalDateTime;

//datos que se van a enviar
public record CarritoResponseDTO(
    Long id,
    Long clienteId,
    LocalDateTime fecha,
    String estado
)implements Serializable{}
