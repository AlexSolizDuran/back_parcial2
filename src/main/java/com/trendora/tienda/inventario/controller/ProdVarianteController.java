package com.trendora.tienda.inventario.controller;

import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteRequestDTO;
import com.trendora.tienda.inventario.dto.prodVariante.ProdVarianteResponseDTO;
import com.trendora.tienda.inventario.service.interfaces.IProdVarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/prod-variante")
public class ProdVarianteController {

    @Autowired
    private IProdVarianteService prodVarianteService;

    @GetMapping
    public ResponseEntity<List<ProdVarianteResponseDTO>> getAll() {
        return ResponseEntity.ok(prodVarianteService.listAll());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<ProdVarianteResponseDTO>> getByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(prodVarianteService.findByProductoId(productoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdVarianteResponseDTO> getById(@PathVariable Long id) {
        return prodVarianteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdVarianteResponseDTO> create(@RequestBody ProdVarianteRequestDTO requestDTO) {
        return new ResponseEntity<>(prodVarianteService.create(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdVarianteResponseDTO> update(@PathVariable Long id, @RequestBody ProdVarianteRequestDTO requestDTO) {
        return prodVarianteService.update(id, requestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (prodVarianteService.findById(id).isPresent()) {
            prodVarianteService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}