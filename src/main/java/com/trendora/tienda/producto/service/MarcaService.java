package com.trendora.tienda.producto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendora.tienda.producto.dto.marca.MarcaRequestDTO;
import com.trendora.tienda.producto.dto.marca.MarcaResponseDTO;
import com.trendora.tienda.producto.model.Marca;
import com.trendora.tienda.producto.repository.MarcaRepository;
import com.trendora.tienda.producto.service.interfaces.IMarcaService;

@Service
public class MarcaService implements IMarcaService {

    private final MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaResponseDTO> obtenerTodo() {
        return marcaRepository.findAll()
                .stream()
                // 3. Llamamos a nuestro método privado
                .map(this::toResponseDTO) 
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaResponseDTO> obtenerPorId(Long id) {
        return marcaRepository.findById(id)
                // 3. Llamamos a nuestro método privado
                .map(this::toResponseDTO);
    }

    @Override
    @Transactional
    public MarcaResponseDTO guardar(MarcaRequestDTO marcaDTO) {
        // 3. Llamamos a nuestro método privado
        Marca nuevaMarca = this.toEntity(marcaDTO);
        
        Marca marcaGuardada = marcaRepository.save(nuevaMarca);
        
        // 3. Llamamos a nuestro método privado
        return this.toResponseDTO(marcaGuardada);
    }

    @Override
    @Transactional
    public Optional<MarcaResponseDTO> actualizar(Long id, MarcaRequestDTO marcaDTO) {
        
        return marcaRepository.findById(id)
                .map(marcaExistente -> {
                    // 3. Llamamos a nuestro método privado
                    this.updateFromDTO(marcaDTO, marcaExistente);
                    
                    Marca marcaActualizada = marcaRepository.save(marcaExistente);
                    
                    // 3. Llamamos a nuestro método privado
                    return this.toResponseDTO(marcaActualizada);
                });
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!marcaRepository.existsById(id)) {
            throw new RuntimeException("Marca no encontrada con id: " + id);
        }
        marcaRepository.deleteById(id);
    }

    // --- 4. MÉTODOS PRIVADOS DEL MAPPER ---
    // Esta lógica estaba antes en la clase MarcaMapper
    
    /**
     * Convierte una Entidad Marca a su DTO de Respuesta.
     */
    private MarcaResponseDTO toResponseDTO(Marca marca) {
        if (marca == null) return null;
        return new MarcaResponseDTO(
            marca.getId(),
            marca.getNombre()
        );
    }

    /**
     * Convierte un DTO de Petición a una Entidad Marca (para crear).
     */
    private Marca toEntity(MarcaRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        Marca marca = new Marca();
        marca.setNombre(requestDTO.nombre());
        return marca;
    }

    /**
     * Actualiza una Entidad existente desde un DTO de Petición (para actualizar).
     */
    private void updateFromDTO(MarcaRequestDTO requestDTO, Marca marca) {
        if (requestDTO == null || marca == null) return;
        marca.setNombre(requestDTO.nombre());
    }

   
}