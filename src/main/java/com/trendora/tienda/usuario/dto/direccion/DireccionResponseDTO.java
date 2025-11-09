package com.trendora.tienda.usuario.dto.direccion;

import java.io.Serializable;

public record DireccionResponseDTO(
    Long id,
    String departamento,
    String zona,
    String calle,
    String numeroCasa,
    String referencia,
    Long usuarioId
) implements Serializable{}
