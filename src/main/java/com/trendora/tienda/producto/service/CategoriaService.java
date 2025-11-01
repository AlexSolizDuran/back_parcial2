package com.trendora.tienda.producto.service;

import com.trendora.tienda.producto.dto.categoria.CategoriaRequestDTO;
import com.trendora.tienda.producto.dto.categoria.CategoriaResponseDTO;
import com.trendora.tienda.producto.model.Categoria;
import com.trendora.tienda.producto.repository.CategoriaRepository;
import com.trendora.tienda.producto.service.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
