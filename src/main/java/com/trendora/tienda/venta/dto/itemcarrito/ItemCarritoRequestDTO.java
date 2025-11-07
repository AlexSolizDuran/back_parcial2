package com.trendora.tienda.venta.dto.itemcarrito;

import java.io.Serializable;
//lo que se pude
public record ItemCarritoRequestDTO(
    Long carritoId,
    long prodVariableId,
    Integer cantidad
) implements Serializable{}
