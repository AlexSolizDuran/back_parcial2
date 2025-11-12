package com.trendora.tienda.reporte.generator;

import java.util.List;
import java.util.Map;

import com.trendora.tienda.reporte.dto.ReporteGeneradoDTO;

// Interfaz base para cualquier generador de reportes.
public interface IReportGenerator {
    
    // Recibe los datos y un nombre base para el archivo.
    ReporteGeneradoDTO generar(List<Map<String, Object>> datos, String nombreReporteBase);
}