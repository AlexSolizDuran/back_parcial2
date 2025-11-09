package com.trendora.tienda.venta.controller;

import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoRequestDTO;
import com.trendora.tienda.venta.dto.itemcarrito.ItemCarritoResponseDTO;
import com.trendora.tienda.venta.model.Carrito;
import com.trendora.tienda.venta.service.interfaces.IItemCarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/venta/itemcarrito")
public class ItemCarritoController {

    @Autowired
    private final IItemCarritoService itemCarritoService;

    public ItemCarritoController(IItemCarritoService itemCarritoService) {
        this.itemCarritoService = itemCarritoService;
    }

    @GetMapping
    public ResponseEntity<List<ItemCarritoResponseDTO>> getAllItemCarrito() {
        List<ItemCarritoResponseDTO> items = itemCarritoService.listarTodo();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCarritoResponseDTO> getById(@PathVariable Long id) {
        return itemCarritoService.buscarById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/porcarrito/{carritoId}")
    public ResponseEntity<List<ItemCarritoResponseDTO>> getByCarritoId(@PathVariable Long carritoId) {
        List<ItemCarritoResponseDTO> items = itemCarritoService.buscarByCarrito(new Carrito(carritoId));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ItemCarritoResponseDTO> item = itemCarritoService.buscarById(id);
        if (item.isPresent()) {
            itemCarritoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

