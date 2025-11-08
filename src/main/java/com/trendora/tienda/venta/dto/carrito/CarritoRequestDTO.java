package com.trendora.tienda.venta.dto.carrito;

import java.io.Serializable;
//lo que se pide
public record CarritoRequestDTO(
    Long clienteId
)implements Serializable {}
