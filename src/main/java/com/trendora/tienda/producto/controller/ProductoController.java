package com.trendora.tienda.producto.controller;

import com.trendora.tienda.producto.dto.producto.ProductoRequestDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;
import com.trendora.tienda.producto.service.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto/producto")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> createProducto(@RequestBody ProductoRequestDTO productoRequestDTO) {
        ProductoResponseDTO createdProducto = productoService.create(productoRequestDTO);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(@PathVariable Long id, @RequestBody ProductoRequestDTO productoRequestDTO) {
        return productoService.update(id, productoRequestDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/marca/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByMarcaId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByMarcaId(id));
    }

    @GetMapping("/modelo/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByModeloId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByModeloId(id));
    }

    @GetMapping("/material/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByMaterialId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByMaterialId(id));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByCategoriaId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByCategoriaIdRecursive(id));
    }

    @GetMapping("/etiqueta/{id}")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosByEtiquetaId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findByEtiquetaId(id));
    }
}
