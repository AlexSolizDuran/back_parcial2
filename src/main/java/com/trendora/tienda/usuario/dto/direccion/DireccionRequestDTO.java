package com.trendora.tienda.usuario.dto.direccion;

import java.io.Serializable;

public record DireccionRequestDTO(
    Long usuarioId,
    String departamento,
    String zona,
    String calle,
    String numeroCasa,
    String referencia
) implements Serializable{}
