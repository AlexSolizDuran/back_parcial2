package com.trendora.tienda.usuario.dto.direccion;

import java.io.Serializable;

public record DireccionResponseDTO(
    String departamento,
    String zona,
    String calle,
    String numeroCasa,
    String referencia,
    Long usuarioId
) implements Serializable{}
