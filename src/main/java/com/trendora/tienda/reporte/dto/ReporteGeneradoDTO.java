package com.trendora.tienda.reporte.dto;

public record ReporteGeneradoDTO(
        byte[] contenido,
        String contentType,
        String nombreArchivo) {

}
