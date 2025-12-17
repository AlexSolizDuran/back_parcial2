package com.trendora.tienda.pronostico.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) para enviar una solicitud al microservicio de predicción de demanda.
 * Contiene los datos necesarios para que el modelo de IA realice una predicción.
 */
@Data
@Builder
public class PronosticoRequestDTO {
    private Integer mes;
    private Integer dia_semana;
    private Integer dia_mes;
    private Long producto_id;
    private Long categoria_id;
    private Long modelo_id;
    private Double precio_actual;
}