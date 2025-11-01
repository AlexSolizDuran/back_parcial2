package com.trendora.tienda.producto.controller;

import com.trendora.tienda.producto.dto.etiqueta.EtiquetaRequestDTO;
import com.trendora.tienda.producto.dto.etiqueta.EtiquetaResponseDTO;
import com.trendora.tienda.producto.service.interfaces.IEtiquetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto/etiqueta")
public class EtiquetaController {

    @Autowired
    private IEtiquetaService etiquetaService;

    @GetMapping
    public ResponseEntity<List<EtiquetaResponseDTO>> getAllEtiquetas() {
        return ResponseEntity.ok(etiquetaService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> getEtiquetaById(@PathVariable Long id) {
        return etiquetaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<List<EtiquetaResponseDTO>> getEtiquetasByProductoId(@PathVariable Long id) {
        return ResponseEntity.ok(etiquetaService.findByProductoId(id));
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponseDTO> createEtiqueta(@RequestBody EtiquetaRequestDTO etiquetaRequestDTO) {
        EtiquetaResponseDTO createdEtiqueta = etiquetaService.create(etiquetaRequestDTO);
        return new ResponseEntity<>(createdEtiqueta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquetaResponseDTO> updateEtiqueta(@PathVariable Long id, @RequestBody EtiquetaRequestDTO etiquetaRequestDTO) {
        return etiquetaService.update(id, etiquetaRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtiqueta(@PathVariable Long id) {
        if (etiquetaService.findById(id).isPresent()) {
            etiquetaService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
