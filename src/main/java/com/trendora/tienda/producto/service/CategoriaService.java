package com.trendora.tienda.producto.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.producto.dto.categoria.CategoriaRequestDTO;
import com.trendora.tienda.producto.dto.categoria.CategoriaResponseDTO;
import com.trendora.tienda.producto.dto.categoria.CategoriaTreeDTO;
import com.trendora.tienda.producto.model.Categoria;
import com.trendora.tienda.producto.repository.CategoriaRepository;
import com.trendora.tienda.producto.service.interfaces.ICategoriaService;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listAll() {
        return categoriaRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaTreeDTO> getCategoriaTree() {

        // 1. Cargar TODAS las categorías de la DB (evita Lazy Loading)
        List<Categoria> todasLasCategorias = categoriaRepository.findAll();

        // 2. Un mapa para acceso rápido a cada DTO por su ID
        Map<Long, CategoriaTreeDTO> map = new HashMap<>();

        // 3. Crear DTOs iniciales y ponerlos en el mapa
        // (En este punto, 'hijos' está vacío para todos)
        for (Categoria cat : todasLasCategorias) {
            map.put(cat.getId(), new CategoriaTreeDTO(cat.getId(), cat.getNombre()));
        }

        // 4. Lista para guardar solo las categorías raíz (padre=null)
        List<CategoriaTreeDTO> rootCategorias = new ArrayList<>();

        // 5. Segunda pasada: Conectar los nodos (padres con hijos)
        for (Categoria cat : todasLasCategorias) {
            CategoriaTreeDTO dtoActual = map.get(cat.getId());

            if (cat.getPadre() == null) {
                // Es una categoría raíz
                rootCategorias.add(dtoActual);
            } else {
                // Es una categoría hija. Buscar su padre en el mapa.
                Long padreId = cat.getPadre().getId();
                CategoriaTreeDTO dtoPadre = map.get(padreId);

                if (dtoPadre != null) {
                    // Añadir 'dtoActual' como hijo de 'dtoPadre'
                    dtoPadre.addHijo(dtoActual);
                }
            }
        }

        // 6. Devolver solo la lista de raíces
        // (Las sub-categorías ya están anidadas dentro)
        return rootCategorias;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResponseDTO> findById(Long id) {
        return categoriaRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = new Categoria();
        updateCategoriaFromDTO(categoria, categoriaRequestDTO);
        Categoria savedCategoria = categoriaRepository.save(categoria);
        return convertToResponseDTO(savedCategoria);
    }

    @Override
    @Transactional
    public Optional<CategoriaResponseDTO> update(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    updateCategoriaFromDTO(categoria, categoriaRequestDTO);
                    Categoria updatedCategoria = categoriaRepository.save(categoria);
                    return convertToResponseDTO(updatedCategoria);
                });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    public CategoriaResponseDTO convertToResponseDTO(Categoria categoria) {
        Long padreId = (categoria.getPadre() != null) ? categoria.getPadre().getId() : null;
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNombre(), padreId);
    }

    private void updateCategoriaFromDTO(Categoria categoria, CategoriaRequestDTO dto) {
        categoria.setNombre(dto.nombre());
        if (dto.padreId() != null) {
            Categoria padre = categoriaRepository.findById(dto.padreId())
                    .orElseThrow(() -> new RuntimeException("Parent Categoria not found"));
            categoria.setPadre(padre);
        } else {
            categoria.setPadre(null);
        }
    }
}
