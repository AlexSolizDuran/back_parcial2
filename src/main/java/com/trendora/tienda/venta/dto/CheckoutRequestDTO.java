package com.trendora.tienda.venta.dto;

import java.util.List;

// Lo que envia el frontend
public record CheckoutRequestDTO(
    String metodoPago,
    List<CheckoutItemDTO> items
) {}