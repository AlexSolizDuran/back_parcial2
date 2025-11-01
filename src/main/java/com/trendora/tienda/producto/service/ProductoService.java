package com.trendora.tienda.producto.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.producto.dto.categoria.CategoriaResponseDTO;
import com.trendora.tienda.producto.dto.etiqueta.EtiquetaResponseDTO;
import com.trendora.tienda.producto.dto.material.MaterialReponseDTO;
import com.trendora.tienda.producto.dto.modelo.ModeloResponseDTO;
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

import com.trendora.tienda.producto.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// ... (imports existentes)

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ModeloRepository modeloRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private EtiquetaRepository etiquetaRepository;

    // Inyección de los servicios para reutilizar su lógica
    @Autowired
    private IModeloService modeloService;
    @Autowired
    private ICategoriaService categoriaService;
    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IEtiquetaService etiquetaService;

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
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByMarcaId(Long id) {
        return productoRepository.findByModelo_Marca_Id(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByModeloId(Long id) {
        return productoRepository.findByModeloId(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByMaterialId(Long id) {
        return productoRepository.findByMaterialId(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByCategoriaId(Long id) {
        return productoRepository.findByCategoriaId(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> findByEtiquetaId(Long id) {
        return productoRepository.findByEtiquetasId(id).stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Override
    public ProductoResponseDTO convertToResponseDTO(Producto producto) {
        // Delegar la conversión a los servicios correspondientes
        ModeloResponseDTO modeloDTO = modeloService.convertToResponseDTO(producto.getModelo());
        CategoriaResponseDTO categoriaDTO = categoriaService.convertToResponseDTO(producto.getCategoria());
        MaterialReponseDTO materialDTO = materialService.convertToResponseDTO(producto.getMaterial());
        Set<EtiquetaResponseDTO> etiquetasDTO = producto.getEtiquetas().stream()
                .map(etiquetaService::convertToResponseDTO)
                .collect(Collectors.toSet());

        // Construcción dinámica del nombre
        String nombreCalculado = String.format("%s %s %s",
                modeloDTO.marcaNombre(),
                modeloDTO.nombre(),
                categoriaDTO.nombre());

        return new ProductoResponseDTO(
                producto.getId(),
                nombreCalculado, // Usar el nombre calculado
                producto.getDescripcion(),
                modeloDTO,
                categoriaDTO,
                materialDTO,
                etiquetasDTO
        );
    }

    private Producto convertToEntity(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        updateEntityFromDTO(producto, dto);
        return producto;
    }

    private void updateEntityFromDTO(Producto producto, ProductoRequestDTO dto) {
        Modelo modelo = modeloRepository.findById(dto.modeloId()).orElseThrow(() -> new RuntimeException("Modelo not found"));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId()).orElseThrow(() -> new RuntimeException("Categoria not found"));
        Material material = materialRepository.findById(dto.materialId()).orElseThrow(() -> new RuntimeException("Material not found"));
        Set<Etiqueta> etiquetas = etiquetaRepository.findAllById(dto.etiquetaIds()).stream().collect(Collectors.toSet());

        producto.setDescripcion(dto.descripcion());
        producto.setModelo(modelo);
        producto.setCategoria(categoria);
        producto.setMaterial(material);
        producto.setEtiquetas(etiquetas);
    }
}
