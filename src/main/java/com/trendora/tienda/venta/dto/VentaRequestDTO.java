package com.trendora.tienda.venta.dto;

import java.io.Serializable;
//datos que el cli puede enviar para crear, actualizar una venta
public record VentaRequestDTO(
    Long clienteID,
    Long vendedorID,
    String metodoPago,
    String tipoVenta
    ) 
    implements Serializable{}
