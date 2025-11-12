package com.trendora.tienda.venta.dto;

// Un item simple del carrito
public record CheckoutItemDTO(
    Long prodVarianteId,
    Integer cantidad
) {}
