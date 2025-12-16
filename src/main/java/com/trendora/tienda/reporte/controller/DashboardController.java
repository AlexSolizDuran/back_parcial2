package com.trendora.tienda.reporte.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.producto.repository.ProductoRepository;
import com.trendora.tienda.usuario.repository.UsuarioRepository;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.repository.VentaRepository;

@RestController
@RequestMapping("/reporte/dashboard")
public class DashboardController {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumenGeneral() {
        Map<String, Object> response = new HashMap<>();

        // 1. TARJETAS
        Map<String, Long> cards = new HashMap<>();
        cards.put("totalVentas", ventaRepository.count());
        cards.put("totalProductos", productoRepository.count());
        cards.put("usuariosRegistrados", usuarioRepository.count());
        response.put("cards", cards);

        // 2. DATOS PARA EL GRÁFICO (Últimos 7 días)
        LocalDateTime hace7Dias = LocalDateTime.now().minusDays(7);
        // Usamos el método corregido del repositorio
        List<Venta> ventasSemana = ventaRepository.findByFechaVentaAfter(hace7Dias);

        // --- AQUÍ ESTABA EL ERROR ---
        // Corregimos: getFecha() -> getFechaVenta() y getTotal() -> getMontoTotal()
        Map<String, Double> ventasPorDia = ventasSemana.stream()
            .collect(Collectors.groupingBy(
                (Venta v) -> v.getFechaVenta().toLocalDate().toString(), // Clave: String
                Collectors.summingDouble(v -> v.getMontoTotal() != null ? v.getMontoTotal() : 0.0) // Valor: Double
            ));

        // Rellenar días vacíos
        List<Map<String, Object>> chartData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String fechaStr = LocalDate.now().minusDays(i).toString();
            chartData.add(Map.of(
                "fecha", fechaStr,
                "total", ventasPorDia.getOrDefault(fechaStr, 0.0)
            ));
        }
        response.put("chartData", chartData);

        // 3. TABLA DE ÚLTIMAS VENTAS
        // Usamos el método corregido del repositorio
        List<Venta> ultimasVentas = ventaRepository.findTop5ByOrderByFechaVentaDesc();
        
        List<Map<String, Object>> recentSales = ultimasVentas.stream().map(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getId());
            // Verificamos nulos por seguridad
            map.put("usuario", v.getCliente() != null ? v.getCliente().getNombre() : "Anónimo");
            map.put("total", v.getMontoTotal());
            map.put("fecha", v.getFechaVenta().toString());
            map.put("estado", v.getEstadoPedido());
            return map;
        }).collect(Collectors.toList());
        
        response.put("recentSales", recentSales);

        return ResponseEntity.ok(response);
    }
}