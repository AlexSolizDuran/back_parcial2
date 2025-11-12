package com.trendora.tienda.reporte.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.reporte.dto.IaRequestDTO;
import com.trendora.tienda.reporte.dto.ReporteGeneradoDTO;
import com.trendora.tienda.reporte.service.ReporteService;

@RestController
@RequestMapping("/v1/reporte")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;
    
    /**
     * ENDPOINT 1: /datos
     * Recibe un prompt (IaRequestDTO) y retorna los datos brutos del reporte
     * (List<Map<String, Object>>) como JSON. Usado para frontend tables/charts.
     * * RUTA FRONTEND: POST /api/reporte/datos
     * RUTA BACKEND: POST /v1/reporte/datos
     */
    @PostMapping("/datos")
    public ResponseEntity<List<Map<String, Object>>> obtenerDatosTabla(@RequestBody IaRequestDTO request) {
        
        List<Map<String, Object>> datos = reporteService.obtenerDatosTabla(request.prompt());
        
        return ResponseEntity.ok(datos);
    }

    /**
     * ENDPOINT 2: /descargar
     * Recibe un prompt (IaRequestDTO) que debe especificar el formato
     * (ej. "en PDF") y retorna el archivo binario para su descarga.
     * * RUTA FRONTEND: POST /api/reporte/descargar
     * RUTA BACKEND: POST /v1/reporte/descargar
     */
    @PostMapping("/descargar")
    public ResponseEntity<byte[]> descargarReporte(@RequestBody IaRequestDTO request) {
        
        // 1. Llama al servicio que genera el ReporteGenerado
        ReporteGeneradoDTO reporte = reporteService.generarArchivoReporte(request.prompt());

        // 2. CONSTRUYE LA RESPUESTA HTTP BINARIA
        return ResponseEntity.ok()
                // Indica el tipo MIME (ej. application/pdf, application/xlsx)
                .contentType(MediaType.parseMediaType(reporte.contentType())) 
                
                // Fuerza al navegador a descargar el archivo con el nombre correcto
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + reporte.nombreArchivo() + "\"")
                        
                // Agrega el tamaño del archivo
                .contentLength(reporte.contenido().length) 
                
                // El cuerpo de la respuesta es el archivo binario
                .body(reporte.contenido()); 
    }
}