package com.trendora.tienda.producto.controller;

import com.trendora.tienda.producto.dto.modelo.ModeloRequestDTO;
import com.trendora.tienda.producto.dto.modelo.ModeloResponseDTO;
import com.trendora.tienda.producto.service.interfaces.IModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto/modelo")
public class ModeloController {

    @Autowired
    private IModeloService modeloService;

    @GetMapping
    public ResponseEntity<List<ModeloResponseDTO>> getAllModelos() {
        return ResponseEntity.ok(modeloService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloResponseDTO> getModeloById(@PathVariable Long id) {
        return modeloService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/marca/{id}")
    public ResponseEntity<List<ModeloResponseDTO>> getModelosByMarcaId(@PathVariable Long id) {
        return ResponseEntity.ok(modeloService.findByMarcaId(id));
    }

    @PostMapping
    public ResponseEntity<ModeloResponseDTO> createModelo(@RequestBody ModeloRequestDTO modeloRequestDTO) {
        ModeloResponseDTO createdModelo = modeloService.create(modeloRequestDTO);
        return new ResponseEntity<>(createdModelo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloResponseDTO> updateModelo(@PathVariable Long id, @RequestBody ModeloRequestDTO modeloRequestDTO) {
        return modeloService.update(id, modeloRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelo(@PathVariable Long id) {
        if (modeloService.findById(id).isPresent()) {
            modeloService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
