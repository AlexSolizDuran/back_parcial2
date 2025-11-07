package com.trendora.tienda.venta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoRequestDTO;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.service.interfaces.IItemCarritoService;

@RestController
@RequestMapping("/venta/iteamcarrito")
public class ItemCarritoController {
    @Autowired
    private final IItemCarritoService itemCarritoService;

    public ItemCarritoController(IItemCarritoService itenmCarritoService){
        this.itemCarritoService = itenmCarritoService;
    }

    @GetMapping
    public ResponseEntity<List<ItemCarritoResponseDTO>> getAllItemCarrito() {
        List<ItemCarritoResponseDTO> items = itemCarritoService.listarTodo()
                .stream()
                .map(itemCarritoService::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCarritoResponseDTO> getById(@PathVariable Long id) {
        return itemCarritoService.buscarById(id)
                .map(itemCarritoService::convertToResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/porcarrito/{carritoId}")
    public ResponseEntity<List<ItemCarritoResponseDTO>> getByCarritoId(@PathVariable Long carritoId) {
        List<ItemCarritoResponseDTO> items = itemCarritoService.buscarByCarrito(new Carrito(carritoId))
                .stream()
                .map(itemCarritoService::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<ItemCarritoResponseDTO> create(@RequestBody ItemCarritoRequestDTO dto) {
        ItemCarritoResponseDTO nuevoItem = itemCarritoService.create(dto);
        return ResponseEntity.ok(nuevoItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCarritoResponseDTO> update(@PathVariable Long id, @RequestBody ItemCarritoRequestDTO dto) {
        return itemCarritoService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        itemCarritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
