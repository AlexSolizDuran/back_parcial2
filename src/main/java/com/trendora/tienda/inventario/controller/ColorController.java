package com.trendora.tienda.inventario.controller;

import com.trendora.tienda.inventario.dto.color.ColorRequestDTO;
import com.trendora.tienda.inventario.dto.color.ColorResponseDTO;
import com.trendora.tienda.inventario.service.interfaces.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/color")
public class ColorController {

    @Autowired
    private IColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorResponseDTO>> getAll() {
        return ResponseEntity.ok(colorService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorResponseDTO> getById(@PathVariable Long id) {
        return colorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ColorResponseDTO> create(@RequestBody ColorRequestDTO requestDTO) {
        return new ResponseEntity<>(colorService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorResponseDTO> update(@PathVariable Long id, @RequestBody ColorRequestDTO requestDTO) {
        return colorService.update(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (colorService.findById(id).isPresent()) {
            colorService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
