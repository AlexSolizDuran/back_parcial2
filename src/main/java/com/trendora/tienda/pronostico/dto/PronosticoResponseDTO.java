package com.trendora.tienda.pronostico.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para recibir la respuesta del microservicio de predicción de demanda.
 * Contiene la predicción de ventas para un producto específico.
 */
@Data
public class PronosticoResponseDTO {
    /**
     * El ID del producto para el cual se realizó el pronóstico.
     */
    private Long producto_id;

    /**
     * La cantidad de ventas predichas para el producto.
     */
    private Integer prediccion_ventas;

    /**
     * Un mensaje informativo sobre el pronóstico, como por ejemplo, cómo se generó.
     * Opcionalmente, puede contener un mensaje de error si la predicción falló.
     */
    private String mensaje;
}