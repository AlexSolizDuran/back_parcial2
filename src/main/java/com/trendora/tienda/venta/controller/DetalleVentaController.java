package com.trendora.tienda.venta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaRequestDTO;
import com.trendora.tienda.venta.dto.detalleventa.DetalleVentaResponseDTO;
import com.trendora.tienda.venta.model.Venta;
import com.trendora.tienda.venta.service.interfaces.IDetalleVentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/venta/detalle")
@RequiredArgsConstructor
public class DetalleVentaController {

    private final IDetalleVentaService detalleVentaService;

    @GetMapping
    public ResponseEntity<List<DetalleVentaResponseDTO>> getAll() {
        return ResponseEntity.ok(detalleVentaService.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(detalleVentaService.obtenerById(id));
    }

    @GetMapping("/porventa/{ventaId}")
    public ResponseEntity<List<DetalleVentaResponseDTO>> getByVenta(@PathVariable Long ventaId) {
        Venta venta = new Venta();
        venta.setId(ventaId);
        return ResponseEntity.ok(detalleVentaService.obtenerByVenta(venta));
    }

    @PostMapping
    public ResponseEntity<DetalleVentaResponseDTO> create(@RequestBody DetalleVentaRequestDTO dto) {
        return ResponseEntity.ok(detalleVentaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVentaResponseDTO> update(
        @PathVariable Long id,
        @RequestBody DetalleVentaRequestDTO dto){
        Optional<DetalleVentaResponseDTO> updated = detalleVentaService.update(id, dto);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        detalleVentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

