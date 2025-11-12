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
    private final ObjectMapper objectMapper;

    @Autowired
    @Qualifier("pdfGenerator")
    private IReportGenerator pdfGenerator;

    @Autowired
    @Qualifier("excelGenerator")
    private IReportGenerator excelGenerator;

    // --- INICIO DE LA CORRECCIÓN ---
    // Inyectamos RestClient, JdbcTemplate y ObjectMapper
    // Añadimos @Qualifier para especificar qué bean de RestClient queremos inyectar
    public ReporteService(@Qualifier("aiRestClient") RestClient restClient, JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }
    // --- FIN DE LA CORRECCIÓN ---

    /**
     * Paso 1: Llama al microservicio de Python. Esta función contiene la lógica
     * que está fallando.
     */
    private IaResponseDTO obtenerRespuestaDeIa(String prompt) {

        // 1. Prepara el prompt que espera el modelo T5 ("generar JSON: ...")
        IaRequestDTO requestBody = new IaRequestDTO("generar JSON: "+prompt);

        // --- INICIO CAMBIO ---
        // Construimos la cadena JSON manualmente para evitar el problema de serialización
        String jsonBody = "{\"prompt\": \"" + requestBody.prompt() + "\"}";
        System.out.println("Enviando cuerpo JSON manual: " + jsonBody);
        // --- FIN CAMBIO ---

        try {
            return restClient.post()
                    .uri("/generar-sql")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonBody) // <--- Enviamos la cadena String directamente
                    .retrieve()
                    .body(IaResponseDTO.class);
        } catch (Exception e) {
            // Captura el error y lanza una excepción más amigable para el frontend
            System.err.println("ERROR al conectar/procesar con microservicio AI: " + e.getMessage());
            // Si el error es 422, indica un problema en el lado del microservicio Python.
            throw new RuntimeException("Error en la validación del microservicio AI. El campo 'prompt' no fue aceptado.", e);
        }
    }

    // El resto de la clase ReporteService permanece sin cambios
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerDatosTabla(String prompt) {

        IaResponseDTO respuestaDeIa = obtenerRespuestaDeIa(prompt);

        if (respuestaDeIa == null || respuestaDeIa.sql() == null || respuestaDeIa.sql().isEmpty()) {
            throw new RuntimeException("La IA no pudo generar un SQL válido.");
        }

        return jdbcTemplate.queryForList(respuestaDeIa.sql());
    }

    @Transactional(readOnly = true)
    public ReporteGeneradoDTO generarArchivoReporte(String prompt) {

        IaResponseDTO respuestaDeIa = obtenerRespuestaDeIa(prompt);

        if (respuestaDeIa == null || respuestaDeIa.sql() == null || respuestaDeIa.sql().isEmpty()) {
            throw new RuntimeException("La IA no pudo generar un SQL válido.");
        }

        List<Map<String, Object>> datosReporte = jdbcTemplate.queryForList(respuestaDeIa.sql());

        String formato = respuestaDeIa.formato().toLowerCase();
        String nombreBase = "reporte_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        switch (formato) {
            case "pdf":
                return pdfGenerator.generar(datosReporte, nombreBase);

            case "excel":
                return excelGenerator.generar(datosReporte, nombreBase);

            case "json":
                try {
                    byte[] jsonBytes = objectMapper.writeValueAsBytes(datosReporte);
                    return new ReporteGeneradoDTO(jsonBytes, "application/json", nombreBase + ".json");
                } catch (Exception e) {
                    throw new RuntimeException("Error al serializar a JSON", e);
                }

            default:
                throw new UnsupportedOperationException("Formato de archivo '" + formato + "' no soportado.");
        }
    }
}