package com.trendora.tienda.reporte.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendora.tienda.reporte.dto.IaRequestDTO;
import com.trendora.tienda.reporte.dto.IaResponseDTO;
import com.trendora.tienda.reporte.dto.ReporteGeneradoDTO;
import com.trendora.tienda.reporte.generator.IReportGenerator;

@Service
public class ReporteService {

    private final RestClient restClient;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper; // Para manejar la serialización de JSON

    // Inyección de los generadores por su nombre de Bean
    @Autowired
    @Qualifier("pdfGenerator") 
    private IReportGenerator pdfGenerator;
    
    @Autowired
    @Qualifier("excelGenerator") 
    private IReportGenerator excelGenerator;

    public ReporteService(RestClient restClient, JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Paso 1: Llama al microservicio de Python (Factor común para ambos endpoints).
     */
    private IaResponseDTO obtenerRespuestaDeIa(String promptUsuario) {
        String promptParaIa = "generar JSON: " + promptUsuario;
        IaRequestDTO requestBody = new IaRequestDTO(promptParaIa);
        System.out.println("XXXXXXXXXXXXXXXXXXXXX " + requestBody + " XXXXXXXXXXXXXXX");

        // Llamada síncrona al microservicio de FastAPI
        return restClient.post()
                .uri("/generar-sql")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(IaResponseDTO.class); 
    }

    /**
     * [ENDPOINT 1: TABLA / JSON] Obtiene el SQL de la IA y devuelve los datos brutos.
     * Usado por el frontend para ARMOR LA TABLA o para datos simples.
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerDatosTabla(String prompt) {
        
        IaResponseDTO respuestaDeIa = obtenerRespuestaDeIa(prompt);
        
        if (respuestaDeIa == null || respuestaDeIa.sql() == null || respuestaDeIa.sql().isEmpty()) {
            throw new RuntimeException("La IA no pudo generar un SQL válido.");
        }
        
        // Ejecutar el SQL y devolver la lista de resultados
        return jdbcTemplate.queryForList(respuestaDeIa.sql());
    }

    /**
     * [ENDPOINT 2: ARCHIVO BINARIO] Llama a la IA, ejecuta el SQL, y genera el archivo.
     * Usado para descargar PDF o Excel.
     */
    @Transactional(readOnly = true)
    public ReporteGeneradoDTO generarArchivoReporte(String prompt) {
        
        IaResponseDTO respuestaDeIa = obtenerRespuestaDeIa(prompt);
        
        if (respuestaDeIa == null || respuestaDeIa.sql() == null || respuestaDeIa.sql().isEmpty()) {
            throw new RuntimeException("La IA no pudo generar un SQL válido.");
        }

        // Ejecutar SQL
        List<Map<String, Object>> datosReporte = jdbcTemplate.queryForList(respuestaDeIa.sql());
        
        String formato = respuestaDeIa.formato().toLowerCase();
        String nombreBase = "reporte_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // Delegar la generación
        switch (formato) {
            case "pdf":
                return pdfGenerator.generar(datosReporte, nombreBase);
                
            case "excel":
                return excelGenerator.generar(datosReporte, nombreBase);
                
            case "json":
                // Si el usuario pide JSON para descarga (Opción avanzada)
                try {
                    byte[] jsonBytes = objectMapper.writeValueAsBytes(datosReporte);
                    return new ReporteGeneradoDTO(jsonBytes, "application/json", nombreBase + ".json");
                } catch (Exception e) {
                    throw new RuntimeException("Error al serializar a JSON", e);
                }
                
            default:
                // Si la IA devuelve un formato que no manejamos
                throw new UnsupportedOperationException("Formato de archivo '" + formato + "' no soportado.");
        }
    }
}