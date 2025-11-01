package com.trendora.tienda.producto.controller;

import com.trendora.tienda.producto.dto.material.MaterialReponseDTO;
import com.trendora.tienda.producto.dto.material.MaterialRequestDTO;
import com.trendora.tienda.producto.service.interfaces.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto/material")
public class MaterialController {

    @Autowired
    private IMaterialService materialService;

    @GetMapping
    public ResponseEntity<List<MaterialReponseDTO>> getAllMateriales() {
        return ResponseEntity.ok(materialService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialReponseDTO> getMaterialById(@PathVariable Long id) {
        return materialService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaterialReponseDTO> createMaterial(@RequestBody MaterialRequestDTO materialRequestDTO) {
        MaterialReponseDTO createdMaterial = materialService.create(materialRequestDTO);
        return new ResponseEntity<>(createdMaterial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialReponseDTO> updateMaterial(@PathVariable Long id, @RequestBody MaterialRequestDTO materialRequestDTO) {
        return materialService.update(id, materialRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        if (materialService.findById(id).isPresent()) {
            materialService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
