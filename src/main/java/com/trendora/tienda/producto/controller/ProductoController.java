package com.trendora.tienda.producto.controller;

import java.io.IOException;
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
import com.trendora.tienda.service.CloudinaryService;

@RestController
@RequestMapping("/producto/producto")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Autowired
    private CloudinaryService cloudinaryService;

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

        String urlImagen = null;

        // 3.1. Si la imagen existe, subirla a Cloudinary
        if (imagen != null && !imagen.isEmpty()) {
            try {
                urlImagen = cloudinaryService.uploadImagen(imagen);
            } catch (Exception e) {
                // Manejo básico de error si la subida falla
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir la imagen", e);
            }
        }

        // 3.2. Crear un NUEVO DTO con la URL (porque el 'record' es inmutable)
        //      Usamos la URL de Cloudinary (o null si no se subió imagen)
        ProductoRequestDTO dtoConImagen = new ProductoRequestDTO(
                productoRequestDTO.descripcion(),
                urlImagen, // <-- Aquí se usa la URL de Cloudinary
                productoRequestDTO.modelo(),
                productoRequestDTO.categoria(),
                productoRequestDTO.material(),
                productoRequestDTO.etiquetas()
        );

        // 3.3. Guardar el producto con la URL de la imagen
        ProductoResponseDTO productoCreado = productoService.create(dtoConImagen);

        return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> updateProducto(
            @PathVariable Long id,
            @RequestPart("producto") ProductoRequestDTO productoRequestDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {

        // 1. Obtener los datos del producto ANTES de actualizar
        ProductoResponseDTO productoExistente = productoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        String urlImagenParaGuardar = productoExistente.imagen(); // Mantenemos la URL antigua por defecto
        String urlImagenAntiguaParaBorrar = null;

        // 2. Si se está subiendo una NUEVA imagen...
        if (imagen != null && !imagen.isEmpty()) {
            try {
                // Guardamos la URL antigua para borrarla DESPUÉS de subir la nueva
                urlImagenAntiguaParaBorrar = productoExistente.imagen();

                // Subimos la nueva imagen
                urlImagenParaGuardar = cloudinaryService.uploadImagen(imagen);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir la nueva imagen", e);
            }
        }

        // 3. Crear el DTO con la información actualizada (incluida la nueva URL)
        ProductoRequestDTO dtoConImagenActualizada = new ProductoRequestDTO(
                productoRequestDTO.descripcion(),
                urlImagenParaGuardar, // <-- Aquí va la URL nueva (o la antigua si no se subió nada)
                productoRequestDTO.modelo(),
                productoRequestDTO.categoria(),
                productoRequestDTO.material(),
                productoRequestDTO.etiquetas()
        );

        // 4. Guardar el producto en la BBDD con la nueva URL
        ProductoResponseDTO productoActualizado = productoService.update(id, dtoConImagenActualizada)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error al actualizar el producto en BBDD"));

        // 5. SI TODO SALIÓ BIEN (subida y BBDD), borramos la imagen antigua
        if (urlImagenAntiguaParaBorrar != null) {
            try {
                cloudinaryService.eliminarImagen(urlImagenAntiguaParaBorrar);
            } catch (IOException e) {
                // La app funcionó, pero falló el borrado. Solo log.
                System.err.println("Error al borrar la imagen antigua de Cloudinary: " + e.getMessage());
            }
        }

        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        if (productoService.findById(id).isPresent()) {
            productoService.delete(id); // <-- Esto ya llama a nuestra nueva lógica
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
