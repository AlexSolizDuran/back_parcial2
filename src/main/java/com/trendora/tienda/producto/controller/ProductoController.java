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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.trendora.tienda.producto.dto.producto.ProductoRequestDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;
import com.trendora.tienda.producto.service.interfaces.IProductoService;

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
    public ResponseEntity<ProductoResponseDTO> createProducto(
            @RequestPart("producto") ProductoRequestDTO productoRequestDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        // 1. Crear el producto solo con los datos
        ProductoResponseDTO productoCreado = productoService.create(productoRequestDTO);

        // 2. Si hay imagen, guardarla por separado
        if (imagen != null && !imagen.isEmpty()) {
            // ejemplo: guardamos la imagen en un folder y asociamos al producto
            //String rutaImagen = imagenService.guardarImagen(productoCreado.getId(), imagen);
            //productoCreado.setImagenUrl(rutaImagen); // actualizar DTO con URL
        }

        return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(
            @PathVariable Long id,
            @RequestPart("producto") ProductoRequestDTO productoRequestDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        // 1. Actualizar solo los datos del producto
        ProductoResponseDTO productoActualizado = productoService.update(id, productoRequestDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        // 2. Si hay imagen, guardarla por separado
        if (imagen != null && !imagen.isEmpty()) {
            // Ejemplo: guardar la imagen en un folder y asociarla al producto
            // String rutaImagen = imagenService.guardarImagen(productoActualizado.getId(), imagen);
            // productoActualizado.setImagenUrl(rutaImagen);
        }

        return ResponseEntity.ok(productoActualizado);
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
