package com.trendora.tienda.pronostico.service;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.trendora.tienda.exception.EntityNotFoundException;
import com.trendora.tienda.inventario.repository.ProdVarianteRepository;
import com.trendora.tienda.producto.model.Producto;
import com.trendora.tienda.producto.repository.ProductoRepository;
import com.trendora.tienda.pronostico.dto.PronosticoRequestDTO;
import com.trendora.tienda.pronostico.dto.PronosticoResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PronosticoService {

    private final ProductoRepository productoRepository;
    private final ProdVarianteRepository varianteRepository;
    private final RestClient.Builder restClientBuilder;
    private static final Logger LOGGER = Logger.getLogger(PronosticoService.class.getName());

    @Value("${ia.service.url}") // Asegúrate de tener esto en application.properties
    private String iaUrl;

    public PronosticoResponseDTO predecirDemanda(Long productoId, LocalDate fechaUsuario) {
        // 1. Obtener datos del producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Calcular precio promedio
        Double precioPromedio = varianteRepository.findByProductoId(productoId).stream()
                .mapToDouble(v -> v.getPrecio().doubleValue())
                .average()
                .orElse(0.0);

        // 3. Determinar la fecha objetivo (Si el usuario no envió nada, usamos mañana)
        LocalDate fechaObjetivo = (fechaUsuario != null) ? fechaUsuario : LocalDate.now().plusDays(1);

        // 4. Preparar el JSON para la IA con los datos DE LA FECHA ELEGIDA
        PronosticoRequestDTO request = PronosticoRequestDTO.builder()
                .mes(fechaObjetivo.getMonthValue()) // Ej: 12
                .dia_semana(fechaObjetivo.getDayOfWeek().getValue()) // Ej: 5 (Viernes)
                .dia_mes(fechaObjetivo.getDayOfMonth()) // Ej: 25
                .producto_id(producto.getId())
                .categoria_id(producto.getCategoria().getId())
                .modelo_id(producto.getModelo().getId())
                .precio_actual(precioPromedio)
                .build();

        // 5. Llamar a Python
        return restClientBuilder.build()
                .post()
                .uri(iaUrl)
                .body(request)
                .retrieve()
                .body(PronosticoResponseDTO.class);
    }
}
