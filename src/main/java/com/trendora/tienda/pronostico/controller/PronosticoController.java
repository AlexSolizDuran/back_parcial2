package com.trendora.tienda.pronostico.controller;

import com.trendora.tienda.pronostico.dto.PronosticoResponseDTO;
import com.trendora.tienda.pronostico.service.PronosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/pronostico")
@RequiredArgsConstructor
public class PronosticoController {

    private final PronosticoService pronosticoService;

    @GetMapping("/{productoId}")
    public ResponseEntity<PronosticoResponseDTO> obtenerPronostico(
            @PathVariable Long productoId,
            // Agregamos el parámetro de fecha (formato YYYY-MM-DD)
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        
        return ResponseEntity.ok(pronosticoService.predecirDemanda(productoId, fecha));
    }
}