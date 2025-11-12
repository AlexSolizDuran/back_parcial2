package com.trendora.tienda.reporte.dto;

import java.util.List;

public record IaResponseDTO(
    String sql,
    String formato,
    List<String> columnas
) {}