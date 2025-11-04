package com.trendora.tienda.inventario.controller;

import com.trendora.tienda.inventario.dto.talla.TallaRequestDTO;
import com.trendora.tienda.inventario.dto.talla.TallaResponseDTO;
import com.trendora.tienda.inventario.service.interfaces.ITallaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/talla")
public class TallaController {

    @Autowired
    private ITallaService tallaService;

    @GetMapping
    public ResponseEntity<List<TallaResponseDTO>> getAll() {
        return ResponseEntity.ok(tallaService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TallaResponseDTO> getById(@PathVariable Long id) {
        return tallaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TallaResponseDTO> create(@RequestBody TallaRequestDTO requestDTO) {
        return new ResponseEntity<>(tallaService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TallaResponseDTO> update(@PathVariable Long id, @RequestBody TallaRequestDTO requestDTO) {
        return tallaService.update(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (tallaService.findById(id).isPresent()) {
            tallaService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
