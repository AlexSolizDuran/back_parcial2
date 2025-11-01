package com.trendora.tienda.producto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.producto.dto.marca.MarcaRequestDTO;
import com.trendora.tienda.producto.dto.marca.MarcaResponseDTO;
import com.trendora.tienda.producto.service.interfaces.IMarcaService;

@RestController
@RequestMapping("/producto/marca") // URL base para todos los endpoints de marcas
public class MarcaController {

    // Inyectamos la interfaz del servicio (no la implementación)
    private final IMarcaService marcaService;

    @Autowired
    public MarcaController(IMarcaService marcaService) {
        this.marcaService = marcaService;
    }

    /**
     * Endpoint para OBTENER TODAS las marcas.
     * HTTP GET /api/marcas
     */
    @GetMapping
    public List<MarcaResponseDTO> obtenerTodo() {
        return marcaService.obtenerTodo();
    }

    /**
     * Endpoint para OBTENER UNA marca por su ID.
     * HTTP GET /api/marcas/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> obtenerPorId(@PathVariable Long id) {
        // ResponseEntity nos permite devolver 404 Not Found si no existe
        return marcaService.obtenerPorId(id)
                .map(marca -> ResponseEntity.ok(marca)) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * Endpoint para CREAR una nueva marca.
     * HTTP POST /api/marcas
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve el código 201 Created
    public MarcaResponseDTO guardar(@RequestBody MarcaRequestDTO marcaDTO) {
        // @RequestBody convierte el JSON de la petición en el DTO
        return marcaService.guardar(marcaDTO);
    }

    /**
     * Endpoint para ACTUALIZAR una marca existente.
     * HTTP PUT /api/marcas/5
     */
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> actualizar(
            @PathVariable Long id, 
            @RequestBody MarcaRequestDTO marcaDTO) {
        
        // El servicio 'actualizar' devuelve un Optional, 
        // ideal para este patrón de respuesta.
        return marcaService.actualizar(id, marcaDTO)
                .map(marca -> ResponseEntity.ok(marca)) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    /**
     * Endpoint para ELIMINAR una marca por su ID.
     * HTTP DELETE /api/marcas/5
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            marcaService.eliminar(id);
            // Si tiene éxito, devuelve 204 No Content
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si el servicio lanza la excepción (porque no encontró el ID),
            // devolvemos 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }
}