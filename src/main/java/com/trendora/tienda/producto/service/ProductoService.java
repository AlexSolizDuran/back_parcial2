package com.trendora.tienda.producto.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.producto.dto.producto.ProductoRequestDTO;
import com.trendora.tienda.producto.dto.producto.ProductoResponseDTO;
import com.trendora.tienda.producto.model.Categoria;
import com.trendora.tienda.producto.model.Etiqueta;
import com.trendora.tienda.producto.model.Material;
import com.trendora.tienda.producto.model.Modelo;
import com.trendora.tienda.producto.model.Producto;
import com.trendora.tienda.producto.repository.CategoriaRepository;
import com.trendora.tienda.producto.repository.EtiquetaRepository;
import com.trendora.tienda.producto.repository.MaterialRepository;
import com.trendora.tienda.producto.repository.ModeloRepository;
import com.trendora.tienda.producto.repository.ProductoRepository;
import com.trendora.tienda.producto.service.interfaces.IProductoService;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // --- CAMBIO: Inyectar repositorios para buscar entidades por ID ---
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private EtiquetaRepository etiquetaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listAll() {
        return productoRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> findById(Long id) {
        return productoRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public ProductoResponseDTO create(ProductoRequestDTO dto) {
        Producto producto = convertToEntity(dto);
        return convertToResponseDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public Optional<ProductoResponseDTO> update(Long id, ProductoRequestDTO dto) {
        return productoRepository.findById(id)
                .map(existingProducto -> {
                    updateEntityFromDTO(existingProducto, dto);
                    return convertToResponseDTO(productoRepository.save(existingProducto));
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // (Añadir lógica de verificación si es necesario, ej. si el producto existe)
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    // ... (findByMarcaId, findByModeloId, etc. no cambian) ...
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByMarcaId(Long id) {
        return productoRepository.findByModelo_Marca_Id(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }
    // ... (etc.)

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByEtiquetaId(Long id) {
        return productoRepository.findByEtiquetasId(id)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // --- CAMBIO PRINCIPAL AQUÍ ---
    @Override
    public ProductoResponseDTO convertToResponseDTO(Producto producto) {

        Long modeloId = (producto.getModelo() != null) ? producto.getModelo().getId() : null;
        Long categoriaId = (producto.getCategoria() != null) ? producto.getCategoria().getId() : null;
        Long materialId = (producto.getMaterial() != null) ? producto.getMaterial().getId() : null;

        Set<Long> etiquetasIds = producto.getEtiquetas().stream()
                .map(Etiqueta::getId) // Llama a getId() en cada objeto Etiqueta
                .collect(Collectors.toSet());

        String modeloNombre = (producto.getModelo() != null) ? producto.getModelo().getNombre() : null;
        String categoriaNombre = (producto.getCategoria() != null) ? producto.getCategoria().getNombre() : null;
        String marcaNombre = (producto.getModelo() != null && producto.getModelo().getMarca() != null)
                ? producto.getModelo().getMarca().getNombre()
                : "";

        String nombreCalculado = String.format("%s %s %s",
                marcaNombre,
                modeloNombre,
                categoriaNombre).trim().replaceAll(" +", " "); // Limpia espacios extra

        return new ProductoResponseDTO(
                producto.getId(),
                nombreCalculado,
                producto.getDescripcion(),
                producto.getImagen(), // <-- Ver la nota 3
                modeloId, // Long
                categoriaId, // Long
                materialId, // Long
                etiquetasIds // Set<Long>
        );
    }

    // --- CAMBIO: Este método ahora asume que el DTO trae los IDs ---
    private Producto convertToEntity(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        // Llama al método 'update' que ahora sí funciona
        updateEntityFromDTO(producto, dto);
        return producto;
    }

    // --- CAMBIO PRINCIPAL AQUÍ ---
    private void updateEntityFromDTO(Producto producto, ProductoRequestDTO dto) {

        // Asumimos que tu ProductoRequestDTO tiene métodos como:
        // dto.modeloId(), dto.categoriaId(), dto.materialId(), dto.etiquetaIds()
        // Busca las entidades completas usando los repositorios
        System.out.println(dto);
        Modelo modelo = modeloRepository.findById(dto.modelo())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado con id: " + dto.modelo()));

        Categoria categoria = categoriaRepository.findById(dto.categoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.categoria()));

        Material material = materialRepository.findById(dto.material())
                .orElseThrow(() -> new RuntimeException("Material no encontrado con id: " + dto.material()));

        Set<Etiqueta> etiquetas = etiquetaRepository.findAllById(dto.etiquetas())
                .stream().collect(Collectors.toSet());

        // Opcional: Validar que se encontraron todas las etiquetas
        if (etiquetas.size() != dto.etiquetas().size()) {
            throw new RuntimeException("Una o más etiquetas no se encontraron");
        }

        // Ahora sí, actualiza la entidad Producto
        producto.setDescripcion(dto.descripcion());
        producto.setModelo(modelo);
        producto.setCategoria(categoria);
        producto.setMaterial(material);
        producto.setEtiquetas(etiquetas);
        producto.setImagen(dto.imagen());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByModeloId(Long id) {
        return productoRepository.findByModeloId(id)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByMaterialId(Long id) {
        return productoRepository.findByMaterialId(id)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByCategoriaId(Long id) {
        return productoRepository.findByCategoriaId(id)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByCategoriaIdRecursive(Long categoriaId) {
        Categoria categoriaRaiz = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaId));

        Set<Long> categoriaIds = new HashSet<>();
        findAllChildCategoryIds(categoriaRaiz, categoriaIds);

        List<Producto> productos = productoRepository.findByCategoriaIdIn(categoriaIds);

        return productos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Función auxiliar recursiva para obtener todos los IDs de las categorías hijas.
     */
    private void findAllChildCategoryIds(Categoria categoria, Set<Long> ids) {
        ids.add(categoria.getId()); // Añadir el ID actual

        // Cargar hijos explícitamente para evitar problemas de LAZY loading
        List<Categoria> hijos = categoriaRepository.findAllById(
            categoria.getHijos().stream().map(Categoria::getId).collect(Collectors.toList())
        );

        for (Categoria hijo : hijos) {
            findAllChildCategoryIds(hijo, ids); // Llamada recursiva
        }
    }
    // ------------------------------------
}
