package com.trendora.tienda.venta.dto.itemcarrito;

import java.io.Serializable;
import java.time.LocalDateTime;

//lo que se da
public record ItemCarritoResponseDTO(
    Long id,
    Long carritoId,
    Long prodVariateId,
    Integer cantidad,
    LocalDateTime fecha
) implements Serializable{}
